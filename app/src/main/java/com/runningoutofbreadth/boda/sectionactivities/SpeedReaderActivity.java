package com.runningoutofbreadth.boda.sectionactivities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Syllable;
import com.runningoutofbreadth.boda.db.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SpeedReaderActivity extends AppCompatActivity implements View.OnClickListener, AnswerDialogFragment.AnswerDialogListener {

    private static final String LOG_TAG = SpeedReaderActivity.class.getSimpleName();

    private TextView mHangeulView;
    private TextView mCorrectView;
    private TextView mSlashView;
    private TextView mTotalView;

    public static final String SYLLABLE_COUNT = "Count";
    public static final String DIFFICULTY = "Difficulty";

    private long mDifficulty;
    Handler mHandler;
    Word mAnswer;
    int mCurrentPos;
    int mTotalCount;
    int mCorrectCount;
    List<Word> mWordList;
    private ArrayList<ColoredResult> mResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(null);
            getWindow().setEnterTransition(null);
            supportPostponeEnterTransition();
            final View decor = getWindow().getDecorView();
            decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreDraw() {
                    decor.getViewTreeObserver().removeOnPreDrawListener(this);
                    supportStartPostponedEnterTransition();
                    return true;
                }
            });
        }
        setContentView(R.layout.activity_speed_reader);

        // get all values from intent and assign to member variables
        Intent intent = getIntent();
        mDifficulty = intent.getLongExtra(DIFFICULTY, 3000);
        mTotalCount = intent.getIntExtra(SYLLABLE_COUNT, 10);

        HashSet<Integer> mIdList = Utility.generateListOfIds(Syllable.class, mTotalCount);
        mWordList = Utility.generateWordList(mIdList, Syllable.class);
        mAnswer = selectWordFromList(mWordList, mCurrentPos);

        FrameLayout mRootContainer = (FrameLayout) findViewById(R.id.speed_read_root_container);
        assert mRootContainer != null;
        mRootContainer.setOnClickListener(this);
        // get references to textviews
        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mCorrectView = (TextView) findViewById(R.id.counter_number_correct);
        mSlashView = (TextView) findViewById(R.id.counter_slash);
        mTotalView = (TextView) findViewById(R.id.counter_total);

        Utility.slowFadeIn(mCorrectView);
        Utility.slowFadeIn(mSlashView);

        updateTextView(mHangeulView, mAnswer);

        mTotalView.setText(String.valueOf(mTotalCount));
        mCorrectView.setText(String.valueOf(mCorrectCount));
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
        hideTextViewDelayed(mHangeulView);
//        printInputLanguages();
    }

    /**
     * Hides the textview based on the chosen delay
     **/
    public void hideTextViewDelayed(final TextView textView) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.INVISIBLE);
            }
        }, mDifficulty);
    }

    @Override
    public void onClick(View v) {
//        int viewId = v.getId();
//        Log.v(LOG_TAG, Integer.toString(viewId));
        if (mCurrentPos <= mTotalCount - 1 && mAnswer != null) {
            showAnswerDialog();
        }
    }

    @Override
    public void onDialogAnswerClick(AnswerDialogFragment dialog, String answer) {
        boolean isCorrect = answer.equals(mAnswer.getHangeul()) || answer.equals(mAnswer.getRomanization());
        if (isCorrect) {
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
                updateTextView(mHangeulView, mAnswer);
                hideTextViewDelayed(mHangeulView);
            }
        }

    }

    private int resultColor(boolean isCorrect) {
        return isCorrect ? R.color.color_correct : R.color.color_incorrect;
    }

    public void showAnswerDialog() {
        AnswerDialogFragment dialog = new AnswerDialogFragment();
        dialog.show(getSupportFragmentManager(), "AnswerDialogFragment");
    }

    private void showResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.RESULT_CORRECT, String.valueOf(mCorrectCount));
        intent.putExtra(ResultsActivity.RESULT_TOTAL, String.valueOf(mTotalCount));
        intent.putExtra(ResultsActivity.RESULT_COLORS, mResultsList);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                findViewById(R.id.counter), getString(R.string.counter_transition_name));
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }

    /**
     * Loads an instance of a Word (class), sets its Hangeul String (field)
     * as the corresponding TextView's value.
     **/
    private void updateTextView(TextView textView, Word newWord) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(newWord.getHangeul());
    }

    /**
     * Gets word from main word list. Returns null if position exceeds size of list
     */
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


    /**
     * Convenience method for showing available languages on device
     **/
    private void printInputLanguages() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> ims = imm.getEnabledInputMethodList();

        for (InputMethodInfo method : ims) {
            List<InputMethodSubtype> submethods = imm.getEnabledInputMethodSubtypeList(method, true);
            for (InputMethodSubtype submethod : submethods) {
                if (submethod.getMode().equals("keyboard")) {
                    String currentLocale = submethod.getLocale();
                    Log.i(LOG_TAG, "Available input method locale: " + currentLocale);
                }
            }
        }
    }
}
