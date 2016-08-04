package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Syllable;
import com.runningoutofbreadth.boda.db.Word;

import java.util.Random;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mChoiceOne;
    private TextView mChoiceTwo;
    private TextView mChoiceThree;
    private TextView mChoiceFour;

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
    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mImageView = (ImageView) findViewById(R.id.image);
        mChoiceOne = (TextView) findViewById(R.id.choice_one);
        mChoiceTwo = (TextView) findViewById(R.id.choice_two);
        mChoiceThree = (TextView) findViewById(R.id.choice_three);
        mChoiceFour = (TextView) findViewById(R.id.choice_four);

        // TODO: 7/23/2016 add one more view that tracks correct/incorrect.

        Intent intent = getIntent();
        mModelName = intent.getStringExtra(CATEGORY);

        try {
            // create reference to category
            // param becomes String "com.runningoutofbreadth.boda.db.DBMODEL"
            mModel = Class.forName(getPackageName() + ".db." + mModelName);

//            Log.v(LOG_TAG, mModel.toString());
            // get size of db table
            int randPosMax = (int) SQLite.selectCountOf().from(mModel).count();
            Word newWord = Utility.wordSelector(Utility.randInt(0, randPosMax), mModel);

//            Log.v(LOG_TAG, newWord[WORDSELECTOR_HANGEUL] + " " + newWord[WORDSELECTOR_ROMANIZATION]);
            mAnswer = newWord.getHangeul();
            String[] choices = createChoices(mAnswer);
            updateViewsForMultipleChoice(choices);
            int resId = getResources().getIdentifier(newWord.getImageId(), "drawable", getPackageName());
            Glide.with(this)
                    .load(resId).error(android.R.drawable.picture_frame)
                    .fitCenter()
                    .into(mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
//        Log.v(LOG_TAG, v.getClass().toString());
        if (v.getClass() == TextView.class || v.getClass() == AppCompatTextView.class){
            TextView temp = (TextView) v;
            if (temp.getText() == mAnswer){
                // increment correct/incorrect counter by one.
            }
            changeWord(mModel);
        }
    }


    /**
     * Takes the randomized choices and places them in view
     **/
    private void updateViewsForMultipleChoice(String[] choices) {
        mChoiceOne.setText(choices[0]);
        mChoiceTwo.setText(choices[1]);
        mChoiceThree.setText(choices[2]);
        mChoiceFour.setText(choices[3]);
    }


    /**
     * Generate variety of answers based on complexity of word
     **/
    private String[] createChoices(String answer) {
        String[] choices = new String[4];
        choices[0] = answer;
        choices[1] = tweakAnswer(answer);
        if (answer.length() > 1) {
            if (answer.length() > 2) {
                choices[2] = mixUpAnswer(answer);
                choices[3] = tweakAnswer(mixUpAnswer(answer));
            } else {
                choices[2] = mixUpAnswer(answer);
                choices[3] = tweakAnswer(answer);
            }
        } else {
            choices[2] = tweakAnswer(answer);
            choices[3] = tweakAnswer(answer);
        }
//        for (String choice : choices) {
//            Log.v(LOG_TAG, choice);
//        }
        shuffleArray(choices);
        return choices;
    }

    /**
     * Generate an answer in which only one syllable is changed
     **/

    private String tweakAnswer(String answer) {
        String[] syllables = answer.split("");
        syllables[syllables.length - 1] = Utility.randomSyllable();
        StringBuilder builder = new StringBuilder();
        for (String s : syllables) {
            builder.append(s);
        }
        if (builder.toString().equals(answer)) {
            return tweakAnswer(answer);
        } else {
            return builder.toString();
        }
    }

    /**
     * Generate a shuffled answer
     **/
    private String mixUpAnswer(String answer) {
        String[] jumbledAnswer = answer.split("");
        shuffleArray(jumbledAnswer);

        StringBuilder builder = new StringBuilder();
        for (String s : jumbledAnswer) {
            builder.append(s);
        }
        if (builder.toString() != answer) {
            return builder.toString();
        } else {
            return mixUpAnswer(answer);
        }
    }

    /**
     * Shuffles all of the strings in a given array
     **/
    private void shuffleArray(String[] choices) {
        Random random = new Random();
        for (int i = choices.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            String temp = choices[index];
            choices[index] = choices[i];
            choices[i] = temp;
        }
    }

    public void changeWord(Class model) {
        int newRandPosMax = (int) SQLite.selectCountOf().from(model).count();
        Word newWord = Utility.wordSelector(Utility.randInt(0, newRandPosMax), model);
        mAnswer = newWord.getHangeul();
        updateViewsForMultipleChoice(createChoices(mAnswer));
        if (model != Syllable.class) {
            int resId = getResources().getIdentifier(newWord.getImageId(), "drawable", getPackageName());
            Utility.glideLoadImage(this, resId, mImageView);
        }
    }
}
