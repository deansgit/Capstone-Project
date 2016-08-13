package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Idiom;
import com.runningoutofbreadth.boda.db.Syllable;
import com.runningoutofbreadth.boda.db.Vocabulary;
import com.runningoutofbreadth.boda.db.Word;

import java.util.HashSet;
import java.util.Set;

import me.grantland.widget.AutofitTextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FlashcardActivity extends AppCompatActivity {
    private static final String LOG_TAG = FlashcardActivity.class.getSimpleName();
    private static final String PREFS_FILENAME = "BodaPrefsFile";

    private ImageView mImageView;
    private AutofitTextView mHangeulView;
    private AutofitTextView mRomanizationView;
    private AutofitTextView mTranslationView;

    public static final String CATEGORY = "Category";
    public static final String CATEGORY_ANIMALS = "Animal";
    public static final String CATEGORY_SYLLABLES = "Syllable";
    public static final String CATEGORY_NATIONS = "Nation";
    public static final String CATEGORY_IDIOMS = "Idiom";
    public static final String CATEGORY_VOCABULARY = "Vocabulary";

    private String mModelName;
    private Class mModel;
    float initialX;
    Set<Integer> mIdsRead;
    private int mTableMax;
//    private float mFontSize;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                initialX = event.getX();
                break;
            case (MotionEvent.ACTION_MOVE):
                break;
            case (MotionEvent.ACTION_UP):
                float finalX = event.getX();
                Log.v(LOG_TAG, Float.toString(finalX));
                // TODO: 7/19/2016 retain list as it is generated, allow scroll back
                if (Math.abs(initialX - finalX) > 50) {
                    changeWord(mModel);
                    changeFont();
                } else {
                    changeFont();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mModelName = intent.getStringExtra(CATEGORY);

        // TODO: 8/9/2016 refactor models to have hasImage boolean
        if (mModelName.equals(CATEGORY_SYLLABLES)
                || mModelName.equals(CATEGORY_IDIOMS)
                || mModelName.equals(CATEGORY_VOCABULARY)) {
            if (mModelName.equals(CATEGORY_SYLLABLES)) {
                setContentView(R.layout.activity_flashcard_syllables);
            } else {
                setContentView(R.layout.activity_flashcard_idioms);
            }
        } else {
            setContentView(R.layout.activity_flashcard);
            mImageView = (ImageView) findViewById(R.id.image);
        }

        mHangeulView = (AutofitTextView) findViewById(R.id.hangeul);
        mRomanizationView = (AutofitTextView) findViewById(R.id.romanization);
        mTranslationView = (AutofitTextView) findViewById(R.id.translation);

        try {
            // create reference list for bulk setToRead
            mIdsRead = new HashSet();

            // create reference to category
            // param becomes String "com.runningoutofbreadth.boda.db.CATEGORY(aka MODEL)"
            mModel = Class.forName(getPackageName() + ".db." + mModelName);

            // set one master reference for max size of table
            mTableMax = (int) SQLite.selectCountOf().from(mModel).count();

            changeWord(mModel);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeFont() {
        mHangeulView.setTypeface(Utility.diffFont(getApplication()));
    }

    public void changeWord(Class model) {
        Word newWord = Utility.wordSelector(Utility.randInt(0, mTableMax), model);
        if (newWord != null) {
            mHangeulView.setText(newWord.getHangeul());
            mRomanizationView.setText(String.format(getString(R.string.romanization), newWord.getRomanization()));
            if (model != Syllable.class && model != Idiom.class && model != Vocabulary.class) {
                int resId = getResources().getIdentifier(newWord.getImageId(), "drawable", getPackageName());
                Glide.with(this)
                        .load(resId).error(android.R.drawable.picture_frame)
                        .fitCenter()
                        .into(mImageView);
            }
            mTranslationView.setText(newWord.getTranslation().replace(";", "\n"));
            mIdsRead.add(newWord.getsId());
        }



        SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILENAME, 0).edit();
        Utility.writeProgressToPrefs(editor, mModel);
        editor.apply();
    }

}