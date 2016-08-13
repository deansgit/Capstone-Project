package com.runningoutofbreadth.boda.sectionactivities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.Utility;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    public static final String RESULT_CORRECT = "RESULT_CORRECT";
    public static final String RESULT_TOTAL = "RESULT_TOTAL";
    public static final String RESULT_COLORS = "RESULT_COLORS";
    private static final String LOG_TAG = ResultsActivity.class.getSimpleName();

//    LinearLayout mCounterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

//        mCounterView = (LinearLayout) findViewById(R.id.counter);
        TextView headerTextView = (TextView) findViewById(R.id.header);
        TextView correctTextView = (TextView) findViewById(R.id.counter_number_correct);
        TextView totalTextView = (TextView) findViewById(R.id.counter_total);
        ListView resultsView = (ListView) findViewById(R.id.results_listview);

        Intent intent = getIntent();
        String numberCorrectString = intent.getStringExtra(RESULT_CORRECT);
        String numberTotalString = intent.getStringExtra(RESULT_TOTAL);
        ArrayList<ColoredResult> resultsList;
        resultsList = intent.getParcelableArrayListExtra(RESULT_COLORS);
        ResultsAdapter resultsAdapter = new ResultsAdapter(getApplicationContext(),
                R.layout.results_list_item, resultsList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            //help stop the blinking?
            postponeEnterTransition();
            final View decor = getWindow().getDecorView();
            decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreDraw() {
                    decor.getViewTreeObserver().removeOnPreDrawListener(this);
                    startPostponedEnterTransition();
                    return true;
                }
            });
        }

        assert headerTextView != null;
        Utility.slowFadeIn(headerTextView);
        assert correctTextView != null;
        correctTextView.setText(numberCorrectString);
        assert totalTextView != null;
        totalTextView.setText(numberTotalString);
        if (resultsView != null) {
            resultsView.setAdapter(resultsAdapter);
            Utility.slowFadeIn(resultsView);
        }
    }

    // TODO: 8/13/2016 If correct, mark as read in db. If incorrect, mark as unread.

}