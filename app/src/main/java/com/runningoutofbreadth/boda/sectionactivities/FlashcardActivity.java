package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private String mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashcard);

        mImageView = (ImageView) findViewById(R.id.image);
        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mRomanizationView = (TextView) findViewById(R.id.romanization);

        Intent intent = getIntent();
        mModel = intent.getStringExtra(CATEGORY);
        if (intent.getStringExtra(CATEGORY).equals(CATEGORY_SYLLABLES)) {
            // hide image, make syllables bigger
            mImageView.setVisibility(View.INVISIBLE);
            mHangeulView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.standalone_syllable_size));
        } else {
            // set image. // TODO: 7/16/2016 implement Glide to load pics from drawables
        }

        try {
            // create reference to category
            // param becomes String "com.runningoutofbreadth.boda.db.DBMODEL"
            final Class model = Class.forName(getPackageName() + ".db." + mModel);
            int randPos = (int) SQLite.selectCountOf().from(model).count();
            mHangeulView.setText(Utility.wordSelector(Utility.randInt(0, randPos), model));
            mHangeulView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.v(LOG_TAG, model.toString());
                    int newRandPos = (int) SQLite.selectCountOf().from(model).count();
                    mHangeulView.setText(Utility.wordSelector(Utility.randInt(0, newRandPos), model));
                    mHangeulView.setTypeface(Utility.diffFont(getApplication()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}