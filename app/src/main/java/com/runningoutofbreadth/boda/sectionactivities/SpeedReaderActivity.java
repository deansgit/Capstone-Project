package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.transition.Scene;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
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
public class SpeedReaderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = SpeedReaderActivity.class.getSimpleName();
    ViewGroup mRootContainer;
    private TextView mHangeulView;
    private TextView mCounterView;

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
    int mCounter;
    int mCorrect;
    private HashSet<Integer> mIdList;
    List<Word> mWordList;

    Scene mSceneOne;
    Scene mSceneTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_reader);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setContentView(R.layout.activity_speed_reader);
            mRootContainer = (FrameLayout) findViewById(R.id.speed_read_root_container);
            if (mRootContainer != null) {
                mRootContainer.setOnClickListener(this);
            }
            mSceneTwo = Scene.getSceneForLayout(mRootContainer,
                    R.layout.activity_speed_reader_end_scene, this);
        }

        // TODO: 8/4/2016 save user-set syllable count in settings
        // get all values from intent and assign to member variables
        Intent intent = getIntent();
        mDifficulty = intent.getLongExtra(DIFFICULTY, 3000);
        mCounter = intent.getIntExtra(SYLLABLE_COUNT, 10);
        mIdList = Utility.generateListOfIds(Syllable.class, mCounter);
        mWordList = generateWordList(mIdList, Syllable.class);
        mCurrentPos = 0;
        mAnswer = selectWordFromList(mWordList, mCurrentPos);

        // get references to textviews
        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mCounterView = (TextView) findViewById(R.id.counter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mCounterView != null) {
                mCounterView.setTransitionName("COUNTER");
            }
        }

        updateTextView(mHangeulView, mAnswer);

        mCounterView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                animateCorrectCounter();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCurrentPos == mCounter) {
                    transitionToEndScene();
                }
            }
        });
        mCounterView.setText(String.format(getString(R.string.counter), mCorrect, mCounter));
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
        if (mCurrentPos <= mCounter - 1 && mAnswer != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Answer");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            builder.setNeutralButton("Answer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().toString().equals(mAnswer.getHangeul())
                            || input.getText().toString().equals(mAnswer.getRomanization())) {
                        mCorrect++;
                        mCounterView.setText(String.format(getString(R.string.counter), mCorrect, mCounter));
                    }
                    mCurrentPos++;
                    if (mCurrentPos == mCounter) {
                        transitionToEndScene();
                    } else {
                        Log.v(LOG_TAG, Integer.toString(mCurrentPos) + " vs " + Integer.toString(mCounter));
                        mAnswer = selectWordFromList(mWordList, mCurrentPos);
                        if (mAnswer != null) {
                            Log.v(LOG_TAG, mAnswer.getHangeul());
                            updateTextView(mHangeulView, mAnswer);
                            Log.v(LOG_TAG, mAnswer.getRomanization());
                            hideTextViewDelayed(mHangeulView);
                        }
                    }
                }
            });
            builder.show();
        }


    }

    private void animateCorrectCounter() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
        animation.setDuration(1000);
        mCounterView.startAnimation(animation);
    }

    private void transitionToEndScene() {
        Log.v(LOG_TAG, "END SCENE!");

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.RESULTS,
                String.format(getString(R.string.counter), mCorrect, mCounter));
//        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                findViewById(R.id.counter), getString(R.string.counter_transition_name));
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Transition presentScoreTrans = TransitionInflater.from(this)
//                    .inflateTransition(android.R.transition.fade);
//            presentScoreTrans.setDuration(1000);
//            TransitionManager.go(mSceneTwo, presentScoreTrans);
//        } else {
//            mCounterView.setVisibility(View.GONE);
//            mHangeulView.setText(String.format(getString(R.string.counter), mCorrect, mCounter));
//            mHangeulView.setVisibility(View.VISIBLE);
//        }
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
