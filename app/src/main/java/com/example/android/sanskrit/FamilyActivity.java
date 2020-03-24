package com.example.android.sanskrit;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;

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
        setContentView(R.layout.activity_family);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words =  new ArrayList<Word>();

        //words.add("");
        words.add(new Word("father","pitR",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother","mAtR",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("brother","bhrAtRka",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("sister","bhaginI",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("son","putra",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter","putrI",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("grandmother","mAtRkA",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("older sister","agrajA",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("younger brother","anuja",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("grandfather","azrumukha",R.drawable.family_grandfather,R.raw.family_grandfather));


        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter adapter =
                new WordAdapter(this, words, R.color.category_family);

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
                    mMediaPlayer  = MediaPlayer.create(FamilyActivity.this,word.getAudioresourceId());
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
