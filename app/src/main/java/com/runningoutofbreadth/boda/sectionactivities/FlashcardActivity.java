package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FlashcardActivity extends AppCompatActivity {
    private static final String LOG_TAG = FlashcardActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mHangeulView;
    private TextView mRomanizationView;

    public static final String CATEGORY = "Category";
    public static final String CATEGORY_ANIMALS = "Animal";
    public static final String CATEGORY_SYLLABLES = "Syllable";
    public static final String CATEGORY_NATIONS = "Nation";
    public static final String CATEGORY_IDIOMS = "Idiom";

    private static final int WORDSELECTOR_HANGEUL = 0;
    private static final int WORDSELECTOR_ROMANIZATION = 1;
    private static final int WORDSELECTOR_IMAGEID = 2;

    private String mModelName;
    private Class mModel;
    float initialX;

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
                if (initialX - finalX > 50) {
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

        setContentView(R.layout.activity_flashcard);

        mImageView = (ImageView) findViewById(R.id.image);
        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mRomanizationView = (TextView) findViewById(R.id.romanization);

        Intent intent = getIntent();
        mModelName = intent.getStringExtra(CATEGORY);
        if (intent.getStringExtra(CATEGORY).equals(CATEGORY_SYLLABLES)) {
            // hide image, make syllables bigger
            mImageView.setVisibility(View.INVISIBLE);
            mHangeulView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.standalone_syllable_size));
        } else{

        }

        try {
            // create reference to category
            // param becomes String "com.runningoutofbreadth.boda.db.DBMODEL"
            mModel = Class.forName(getPackageName() + ".db." + mModelName);

            Log.v(LOG_TAG, mModel.toString());
            // get size of db table
            int randPosMax = (int) SQLite.selectCountOf().from(mModel).count();
            String[] wordItem = Utility.wordSelector(Utility.randInt(0, randPosMax), mModel);

//            Log.v(LOG_TAG, wordItem[WORDSELECTOR_HANGEUL] + " " + wordItem[WORDSELECTOR_ROMANIZATION]);
            mHangeulView.setText(wordItem[WORDSELECTOR_HANGEUL]);
            mRomanizationView.setText(wordItem[WORDSELECTOR_ROMANIZATION]);
            int resId = getResources().getIdentifier(wordItem[WORDSELECTOR_IMAGEID], "drawable", getPackageName());
            Glide.with(this)
                    .load(resId).error(android.R.drawable.picture_frame)
                    .fitCenter()
                    .into(mImageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeFont() {
        mHangeulView.setTypeface(Utility.diffFont(getApplication()));
    }

    public void changeWord(Class model) {
        int newRandPosMax = (int) SQLite.selectCountOf().from(model).count();
        String[] newWordItem = Utility.wordSelector(Utility.randInt(0, newRandPosMax), model);
        mHangeulView.setText(newWordItem[WORDSELECTOR_HANGEUL]);
        mRomanizationView.setText(newWordItem[WORDSELECTOR_ROMANIZATION]);
        int resId = getResources().getIdentifier(newWordItem[WORDSELECTOR_IMAGEID], "drawable", getPackageName());
        Glide.with(this)
                .load(resId).error(android.R.drawable.picture_frame)
                .fitCenter()
                .into(mImageView);
    }

}