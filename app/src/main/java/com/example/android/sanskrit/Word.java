package com.example.android.sanskrit;

public class Word {
    /** Default translation for the word */
    private String mDefaultTranslation;

    /** Sanskrit translation for the word */
    private String mSanskritTranslation;

    private static final int NO_IMAGE_PROVIDED = -1;

    /** Drawable Image refernce*/
    private int mImageresourceId=NO_IMAGE_PROVIDED;

    private int mAudioresourceId;

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param sanskritTranslation is the word in the Sanskrit language
     */
    public Word(String defaultTranslation, String sanskritTranslation, int mImage, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mSanskritTranslation = sanskritTranslation;
        mImageresourceId =  mImage;
        mAudioresourceId = audioResourceId;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Sanskrit translation of the word.
     */
    public String getSanskritTranslation() {
        return mSanskritTranslation;
    }

    /**
     * Get the Image of the word.
     */
    public int getImageresourceId() {
        return mImageresourceId;
    }

    public boolean hasImage(){
        return mImageresourceId != NO_IMAGE_PROVIDED;
    }

    public int getAudioresourceId() {
        return mAudioresourceId;
    }
}
