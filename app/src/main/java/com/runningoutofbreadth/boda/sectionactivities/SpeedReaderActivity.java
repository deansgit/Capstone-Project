package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SpeedReaderActivity extends AppCompatActivity {

    private TextView mHangeulView;

    public static final String DIFFICULTY = "Difficulty";
    public static final String DIFFICULTY_SLOW = "Slow";
    public static final String DIFFICULTY_NORMAL = "Normal";
    public static final String DIFFICULTY_FAST = "FAST";
    public static final String DIFFICULTY_LUDICROUS = "Ludicrous";



    private static final int WORDSELECTOR_HANGEUL = 0;
    private static final int WORDSELECTOR_ROMANIZATION = 1;
    private static final int WORDSELECTOR_IMAGEID = 2;

    private long mDifficulty;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speed_reader);

        mHangeulView = (TextView) findViewById(R.id.hangeul);
        mHangeulView.setText(Utility.randomSyllable());

        Intent intent = getIntent();
        mDifficulty = intent.getLongExtra(DIFFICULTY, 3000);
        hideHangeul();


    }

    public void hideHangeul(){
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, mDifficulty);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHangeulView.setVisibility(View.INVISIBLE);
        }
    };
}
