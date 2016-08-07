package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;

public class ResultsActivity extends AppCompatActivity {

    public static final String RESULTS = "Results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView headerTextView = (TextView) findViewById(R.id.header);
        TextView resultsTextView = (TextView) findViewById(R.id.results);

        Intent intent = getIntent();
        String resultsString = resultsString = intent.getStringExtra(RESULTS);

        headerTextView.setAlpha(0f);
        headerTextView.animate()
                .alpha(1f)
                .setDuration(2000)
                .setListener(null);
        headerTextView.setVisibility(View.VISIBLE);
        resultsTextView.setText(resultsString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}