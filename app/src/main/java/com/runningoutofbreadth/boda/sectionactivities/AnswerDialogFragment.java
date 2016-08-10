package com.runningoutofbreadth.boda.sectionactivities;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.runningoutofbreadth.boda.R;

/**
 * Dialog Fragment used to contain the AlertDialog
 **/
public class AnswerDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AnswerDialogFragment.class.getSimpleName();

    AnswerDialogListener mListener;
    EditText input;

    /**
     * Callback to receive input
     **/
    public interface AnswerDialogListener {
        void onDialogAnswerClick(AnswerDialogFragment dialog, String answer);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AnswerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AnswerDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootDialogView = inflater.inflate(R.layout.dialogfragment_answer, null);
        builder.setView(rootDialogView);
        input = (EditText) rootDialogView.findViewById(R.id.answer_edittext);
        final Button answerButton = (Button) rootDialogView.findViewById(R.id.answer_button);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogAnswerClick(AnswerDialogFragment.this, input.getText().toString());
                hideKeyboardFrom(getContext(), input);
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (input != null) {
            input.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboardFrom(getContext(), input);
                }
            }, 300);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getDialog() != null) {
            hideKeyboardFrom(getContext(), getDialog().getCurrentFocus());
            Log.v(LOG_TAG, "hideKeyboardCalled onDismiss");
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        // make sure keyboard is hidden when user leaves app
        if (getDialog() != null) {
            hideKeyboardFrom(getContext(), getDialog().getCurrentFocus());
            Log.v(LOG_TAG, "hideKeyboardCalled onDestroyView");
        }
        super.onDestroyView();
    }

    /**
     * Convenience method for showing keyboard while in a fragment.
     **/
    public static void showKeyboardFrom(Context context, View view) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Convenience method for hiding keyboard while in a fragment.
     **/
    public static void hideKeyboardFrom(Context context, View view) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }
}
