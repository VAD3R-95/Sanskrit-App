package com.example.android.sanskrit;

import android.content.ClipData;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp){
            releaseMediaPlayer();
        }

    };

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioChangeListener =
        new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange){
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                 focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //pause playback
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);

            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //resume playback
                mMediaPlayer.start();

            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //stop playback
             releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);


        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words =  new ArrayList<Word>();

        //words.add("");
        words.add(new Word("one","ekam",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two","dve",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three","trayaH",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","catur",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five","panch",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six","SaS",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven","sapta",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight","aSTa",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine","navn",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten","daza",R.drawable.number_ten,R.raw.number_ten));

    //"ekam" "dve"  "trayaH" "catur" "panch" "SaS" "sapta" "aSTa" "navn" "daza"

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter adapter =
                new WordAdapter(this, words,R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word =   words.get(i);
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // we have audio focus
                    mMediaPlayer  = MediaPlayer.create(NumbersActivity.this,word.getAudioresourceId());
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer( ) {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioChangeListener);
        }
    }

}
