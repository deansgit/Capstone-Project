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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
//    public static final String DIFFICULTY_SLOW = "Slow";
//    public static final String DIFFICULTY_NORMAL = "Normal";
//    public static final String DIFFICULTY_FAST = "FAST";
//    public static final String DIFFICULTY_LUDICROUS = "Ludicrous";


    private long mDifficulty;
    Handler mHandler;
    Word mAnswer;
    int mCurrentPos;
    int mTotal;
    int mCorrect;
    private HashSet<Integer> mIdList;
    List<Word> mWordList;

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


        // TODO: 8/4/2016 save user-set syllable count in settings
        // get all values from intent and assign to member variables
        Intent intent = getIntent();
        mDifficulty = intent.getLongExtra(DIFFICULTY, 3000);
        mTotal = intent.getIntExtra(SYLLABLE_COUNT, 10);
        mIdList = Utility.generateListOfIds(Syllable.class, mTotal);
        mWordList = generateWordList(mIdList, Syllable.class);
        mCurrentPos = 0;
        mAnswer = selectWordFromList(mWordList, mCurrentPos);

        FrameLayout mRootContainer = (FrameLayout) findViewById(R.id.speed_read_root_container);
        assert mRootContainer != null;
        mRootContainer.setOnClickListener(this);
        // get references to textviews
        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mCorrectView = (TextView) findViewById(R.id.speedreader_number_correct);
        mSlashView = (TextView) findViewById(R.id.speedreader_slash);
        mTotalView = (TextView) findViewById(R.id.speedreader_total);

        Utility.slowFadeIn(mCorrectView);
        Utility.slowFadeIn(mSlashView);

        updateTextView(mHangeulView, mAnswer);

        mTotalView.setText(String.valueOf(mTotal));
        mCorrectView.setText(String.valueOf(mCorrect));
        mCorrectView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                animateCorrectCounter();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCurrentPos == mTotal) {
                    transitionToEndScene();
                }
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
        if (mCurrentPos <= mTotal - 1 && mAnswer != null) {
            showAnswerDialog();
        }
    }

    @Override
    public void onDialogAnswerClick(AnswerDialogFragment dialog, String answer) {
        if (answer.equals(mAnswer.getHangeul()) || answer.equals(mAnswer.getRomanization())) {
            mCorrect++;
            mCorrectView.setText(String.valueOf(mCorrect));
        }
        mCurrentPos++;
        if (mCurrentPos == mTotal) {
            transitionToEndScene();
        } else {
            Log.v(LOG_TAG, Integer.toString(mCurrentPos) + " vs " + Integer.toString(mTotal));
            mAnswer = selectWordFromList(mWordList, mCurrentPos);
            if (mAnswer != null) {
                updateTextView(mHangeulView, mAnswer);
                hideTextViewDelayed(mHangeulView);
            }
        }
    }

    public void showAnswerDialog() {
        AnswerDialogFragment dialog = new AnswerDialogFragment();
        dialog.show(getSupportFragmentManager(), "AnswerDialogFragment");
    }

    private void animateCorrectCounter() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
        animation.setDuration(1000);
        mTotalView.startAnimation(animation);
    }

    private void transitionToEndScene() {
        Log.v(LOG_TAG, "END SCENE!");
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.RESULT_CORRECT,
                String.valueOf(mCorrect));
        intent.putExtra(ResultsActivity.RESULT_TOTAL,
                String.valueOf(mTotal));
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
     * Convenience method for iterating through the Set of table IDs
     * to retrieve the Word items.
     */
    private List<Word> generateWordList(HashSet<Integer> idList, Class category) {
        List<Word> wordList = new ArrayList<>();
        for (int id : idList) {
            wordList.add(Utility.queryWordById(id, category));
        }
        return wordList;
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
