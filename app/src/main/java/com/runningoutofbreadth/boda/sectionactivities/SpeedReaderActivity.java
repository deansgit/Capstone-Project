package com.runningoutofbreadth.boda.sectionactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Word;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SpeedReaderActivity extends AppCompatActivity implements View.OnClickListener {

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
    int mCounter;
    int mCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speed_reader);
        mAnswer = Utility.randomSyllable();
        mCounter = 0;
        mCorrect = 0;

        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mCounterView = (TextView) findViewById(R.id.counter);

        mHangeulView.setText(mAnswer.getHangeul());

        Intent intent = getIntent();
        mDifficulty = intent.getLongExtra(DIFFICULTY, 3000);
        mCounter = intent.getIntExtra(SYLLABLE_COUNT, 10);
        hideHangeul();

    }

    public void hideHangeul() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, mDifficulty);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHangeulView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Answer");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Answer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals(mAnswer.getHangeul())
                        || input.getText().toString().equals(mAnswer.getRomanization())) {
                    mCorrect++;
                }
                mHangeulView.setVisibility(View.VISIBLE);
            }
        });
    }
}
