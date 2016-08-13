package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Animal;
import com.runningoutofbreadth.boda.db.Nation;
import com.runningoutofbreadth.boda.db.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mChoiceOne;
    private TextView mChoiceTwo;
    private TextView mChoiceThree;
    private TextView mChoiceFour;
    private TextView mCorrectView;
    private TextView mSlashView;
    private TextView mTotalView;

    public static final String CATEGORY = "Category";

    private String mModelName;
    private Class mModel;
    private Word mAnswer;

    // for the counter
    int mCurrentPos;
    int mTotalCount;
    int mCorrectCount;

    List<Word> mWordList;
    ArrayList<ColoredResult> mResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // get views for correct/total counter
        mCorrectView = (TextView) findViewById(R.id.counter_number_correct);
        mSlashView = (TextView) findViewById(R.id.counter_slash);
        mTotalView = (TextView) findViewById(R.id.counter_total);

        // get views for quiz parts
        mImageView = (ImageView) findViewById(R.id.image);
        mChoiceOne = (TextView) findViewById(R.id.choice_one);
        mChoiceTwo = (TextView) findViewById(R.id.choice_two);
        mChoiceThree = (TextView) findViewById(R.id.choice_three);
        mChoiceFour = (TextView) findViewById(R.id.choice_four);

        mChoiceOne.setOnClickListener(this);
        mChoiceTwo.setOnClickListener(this);
        mChoiceThree.setOnClickListener(this);
        mChoiceFour.setOnClickListener(this);

        // TODO: 7/23/2016 add one more view that tracks correct/incorrect.
        mTotalCount = 10;

        mCorrectView.setText(String.valueOf(mCorrectCount));
        mTotalView.setText(String.valueOf(mTotalCount));

        mCorrectView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Utility.animatePulse(getApplicationContext(), mCorrectView);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Intent intent = getIntent();
        mModelName = intent.getStringExtra(CATEGORY);

        try {
            // create reference to category
            // param becomes String "com.runningoutofbreadth.boda.db.DBMODEL"
            mModel = Class.forName(getPackageName() + ".db." + mModelName);
            HashSet<Integer> mIdList = Utility.generateListOfIds(mModel, mTotalCount);
            mWordList = Utility.generateWordList(mIdList, mModel);
            mAnswer = selectWordFromList(mWordList, mCurrentPos);
            changeWord(mAnswer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Word selectWordFromList(List<Word> wordList, int position) {
        if (!wordList.isEmpty()) {
            if (position >= wordList.size()) {
                return null;
            } else {
                return wordList.get(position);
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
//        Log.v(LOG_TAG, v.getClass().toString());
        if (mCurrentPos <= mTotalCount) {
            if (v.getClass() == TextView.class || v.getClass() == AppCompatTextView.class) {
                boolean isCorrect = ((TextView) v).getText().equals(mAnswer.getHangeul());
                if (isCorrect) {
                    // TODO: 8/7/2016 update views to show/hide checks or x's and counter
                    mCorrectCount++;
                    mCorrectView.setText(String.valueOf(mCorrectCount));
                }
                mResultsList.add(mCurrentPos, new ColoredResult(mAnswer, resultColor(isCorrect)));
                mCurrentPos++;
                if (mCurrentPos == mTotalCount) {
                    showResults();
                } else {
                    mAnswer = selectWordFromList(mWordList, mCurrentPos);
                    if (mAnswer != null) {
                        changeWord(mAnswer);
                    }
                }
            }
        }
    }

    private int resultColor(boolean isCorrect) {
        return isCorrect ? R.color.color_correct : R.color.color_incorrect;
    }

    private void showResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.RESULT_CORRECT,
                String.valueOf(mCorrectCount));
        intent.putExtra(ResultsActivity.RESULT_TOTAL,
                String.valueOf(mTotalCount));
        intent.putExtra(ResultsActivity.RESULT_COLORS, mResultsList);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                findViewById(R.id.counter), getString(R.string.counter_transition_name));
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
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
        if (answer.length() > 2) {
            choices[2] = mixUpAnswer(answer);
            choices[3] = tweakAnswer(mixUpAnswer(answer));
        } else {
            choices[2] = tweakAnswer(answer);
            choices[3] = tweakAnswer(answer);
        }
        shuffleArray(choices);
        return choices;
    }

    /**
     * Generate an answer in which only one syllable is changed
     **/

    private String tweakAnswer(String answer) {
        String[] syllables = answer.split("");
        syllables[syllables.length - 1] = Utility.randomSyllable().getHangeul();
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

    // TODO: 8/9/2016 refactor models to have hasImage boolean
    public void changeWord(Word word) {
        updateViewsForMultipleChoice(createChoices(word.getHangeul()));
        if (word.getClass().equals(Animal.class)
                || word.getClass().equals(Nation.class)) {
            int resId = getResources().getIdentifier(word.getImageId(), "drawable", getPackageName());
            Utility.glideLoadImage(this, resId, mImageView);
        }
    }
}
