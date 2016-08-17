package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.runningoutofbreadth.boda.Utility;
import com.runningoutofbreadth.boda.db.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by SandD on 8/16/2016.
 */
public class ChoiceLoader extends AsyncTaskLoader<List<String>> {

    private static final String TAG = ChoiceLoader.class.getSimpleName();
    Word mAnswer;

    @Override
    public void onContentChanged() {
        Log.d(TAG, "onContentChanged: called");
        super.onContentChanged();
    }

    public ChoiceLoader(Context context, Word answer) {
        super(context);
        this.mAnswer = answer;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {
        return createChoices(mAnswer.getHangeul());
    }

    @Override
    public void deliverResult(List<String> data) {
        super.deliverResult(data);
    }

    /**
     * Generate variety of answers based on complexity of word
     **/
    private ArrayList<String> createChoices(String answer) {
        ArrayList<String> choices = new ArrayList<>();
        choices.add(answer);
        choices.add(tweakAnswer(answer));
        if (answer.length() > 2) {
            choices.add(mixUpAnswer(answer));
            choices.add(tweakAnswer(mixUpAnswer(answer)));
        } else {
            choices.add(tweakAnswer(answer));
            choices.add(tweakAnswer(answer));
        }
        Collections.shuffle(choices);
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
        List<String> jumbledAnswer = new ArrayList<>(Arrays.asList(answer.split("")));
        Collections.shuffle(jumbledAnswer);

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

}
