package com.runningoutofbreadth.boda.sectionactivities;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.runningoutofbreadth.boda.R;

/**
 * Dialog Fragment used to contain the AlertDialog
 **/
public class AnswerDialogFragment extends DialogFragment {

    /**
     * Callback to receive input
     **/
    public interface AnswerDialogListener {
        void onDialogAnswerClick(AnswerDialogFragment dialog, String answer);
    }

    AnswerDialogListener mListener;

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
        final EditText input = (EditText) rootDialogView.findViewById(R.id.answer_edittext);

        builder.setNeutralButton(getString(R.string.answer_dialog_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogAnswerClick(AnswerDialogFragment.this, input.getText().toString());
            }
        });

        return builder.create();
    }
}
