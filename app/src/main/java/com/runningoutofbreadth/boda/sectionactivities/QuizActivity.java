package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mTranslationView;
    private TextView mCorrectView;
    private TextView mSlashView;
    private TextView mTotalView;

    public static final String CATEGORY = "Category";
    public static final String CATEGORY_ANIMALS = "Animal";
    public static final String CATEGORY_SYLLABLES = "Syllable";
    public static final String CATEGORY_NATIONS = "Nation";
    public static final String CATEGORY_IDIOMS = "Idiom";
    public static final String CATEGORY_VOCABULARY = "Vocabulary";

    private String mModelName;
    private Class mModel;
    private Word mAnswer;
    private boolean mHasImage;

    // for the counter
    int mCurrentPos;
    int mTotalCount;
    int mCorrectCount;

    List<Word> mWordList;
    ArrayList<ColoredResult> mResultsList = new ArrayList<>();
    ChoiceLoaderAdapter choiceAdapter;
    ChoiceLoaderAdapter.CheckAnswerListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mModelName = intent.getStringExtra(CATEGORY);
        mTotalCount = 10;

        // TODO: 8/9/2016 refactor models to have hasImage boolean
        if ((mModelName.equals(CATEGORY_SYLLABLES))
                || mModelName.equals(CATEGORY_IDIOMS)
                || mModelName.equals(CATEGORY_VOCABULARY)) {
            if (mModelName.equals(CATEGORY_SYLLABLES)) {
                setContentView(R.layout.activity_quiz_syllables);
            } else {
                setContentView(R.layout.activity_quiz_idioms);
            }
        } else {
            mHasImage = true;
            setContentView(R.layout.activity_quiz);
            mImageView = (ImageView) findViewById(R.id.image);
        }

        // get views for correct/total counter
        mCorrectView = (TextView) findViewById(R.id.counter_number_correct);
        mSlashView = (TextView) findViewById(R.id.counter_slash);
        mTotalView = (TextView) findViewById(R.id.counter_total);

        // get views for quiz parts
        mTranslationView = (TextView) findViewById(R.id.translation);

        RecyclerView choicesListView = (RecyclerView) findViewById(R.id.choices_listview);
        if (choicesListView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            try {
                // create reference to category
                // param becomes String "com.runningoutofbreadth.boda.db.DBMODEL"
                mModel = Class.forName(getPackageName() + ".db." + mModelName);
                HashSet<Integer> mIdList = Utility.generateListOfIds(mModel, mTotalCount);
                mWordList = Utility.generateWordList(mIdList, mModel);
                mAnswer = selectWordFromList(mWordList, mCurrentPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            choicesListView.setLayoutManager(layoutManager);
            choiceAdapter = new ChoiceLoaderAdapter(getApplicationContext());

            // instantiate CheckAnswerListener
            mListener = new ChoiceLoaderAdapter.CheckAnswerListener() {
                @Override
                public void checkAnswer(View view) {
                    if (mCurrentPos <= mTotalCount) {
                        if (view.getClass() == TextView.class || view.getClass() == AppCompatTextView.class) {
                            boolean isCorrect = ((TextView) view).getText().equals(mAnswer.getHangeul());
                            if (isCorrect) {
                                mCorrectCount++;
                                mCorrectView.setText(String.valueOf(mCorrectCount));
                            } else {
                                Utility.markAsRead(mAnswer, false);
                            }
                            mResultsList.add(mCurrentPos, new ColoredResult(mAnswer, resultColor(isCorrect)));
                            mCurrentPos++;
                            if (mCurrentPos == mTotalCount) {
                                showResults();
                            } else {
                                mAnswer = selectWordFromList(mWordList, mCurrentPos);
                                if (mAnswer != null) {
                                    changeWord(mAnswer, mHasImage);
                                    getSupportLoaderManager().restartLoader(R.id.choice_loader_id, null, loaderCallbacks);
                                }
                            }
                        }
                    }
                }
            };
            choiceAdapter.setOnCheckAnswerListener(mListener);
            choicesListView.setAdapter(choiceAdapter);
            getSupportLoaderManager().initLoader(R.id.choice_loader_id, null, loaderCallbacks);
        }

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
    }

//    @Override
//    public void onClick(View v) {
////        Log.v(LOG_TAG, v.getClass().toString());
//        if (mCurrentPos <= mTotalCount) {
//            if (v.getClass() == TextView.class || v.getClass() == AppCompatTextView.class) {
//                boolean isCorrect = ((TextView) v).getText().equals(mAnswer.getHangeul());
//                if (isCorrect) {
//                    mCorrectCount++;
//                    mCorrectView.setText(String.valueOf(mCorrectCount));
//                } else {
//                    Utility.markAsRead(mAnswer, false);
//                }
//                mResultsList.add(mCurrentPos, new ColoredResult(mAnswer, resultColor(isCorrect)));
//                mCurrentPos++;
//                if (mCurrentPos == mTotalCount) {
//                    showResults();
//                } else {
//                    mAnswer = selectWordFromList(mWordList, mCurrentPos);
//                    if (mAnswer != null) {
//                        changeWord(mAnswer, mHasImage);
//                    }
//                }
//            }
//        }
//    }

    private int resultColor(boolean isCorrect) {
        return isCorrect ? R.color.color_correct : R.color.color_incorrect;
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

    private void showResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.RESULT_CORRECT, String.valueOf(mCorrectCount));
        intent.putExtra(ResultsActivity.RESULT_TOTAL, String.valueOf(mTotalCount));
        intent.putExtra(ResultsActivity.RESULT_COLORS, mResultsList);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                findViewById(R.id.counter), getString(R.string.counter_transition_name));
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        finish();
    }

//    /**
//     * Takes the randomized choices and places them in view
//     **/
//    private void updateViewsForMultipleChoice(String[] choices) {
//        mTranslationView.setText(mAnswer.getTranslation().replace(";", "\n"));
//        mChoiceOne.setText(choices[0]);
//        mChoiceTwo.setText(choices[1]);
//        mChoiceThree.setText(choices[2]);
//        mChoiceFour.setText(choices[3]);
//    }

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
        if (builder.toString().equals(answer)) {
            return mixUpAnswer(answer);
        } else {
            return builder.toString();
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
    public void changeWord(Word word, boolean hasImage) {
        mTranslationView.setText(word.getTranslation().replace(";", "\n"));
        if (hasImage && mImageView != null) {
            int resId = getResources().getIdentifier(word.getImageId(), "drawable", getPackageName());
            Utility.glideLoadImage(this, resId, mImageView);
        }
    }

    // TODO: 8/16/2016 init a Loader to async pull data in each activity/frag
    private LoaderManager.LoaderCallbacks<List<String>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<String>>() {

        @Override
        public Loader<List<String>> onCreateLoader(int id, Bundle args) {
            return new ChoiceLoader(getApplicationContext(), mAnswer);
        }

        @Override
        public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
            Log.d(TAG, "onLoadFinished: called");
            changeWord(mAnswer, mHasImage);
            choiceAdapter.loadNewChoices(data);
        }

        @Override
        public void onLoaderReset(Loader<List<String>> loader) {
            choiceAdapter.loadNewChoices(Collections.EMPTY_LIST);
        }
    };

}
