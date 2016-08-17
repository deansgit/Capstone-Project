package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create and populate the randomized multiple choices to go into the Quiz's RecylerView
 */
public class ChoiceLoaderAdapter extends RecyclerView.Adapter<ChoiceLoaderAdapter.ViewHolder> {

    private static final String TAG = ChoiceLoaderAdapter.class.getSimpleName();
    private List<String> choices = new ArrayList<>();
    private LayoutInflater inflater;
    CheckAnswerListener mListener;

    public ChoiceLoaderAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void loadNewChoices(List<String> newChoices) {
        this.choices.clear();
        this.choices.addAll(newChoices);
        notifyDataSetChanged();
    }

    public ChoiceLoaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_choice_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(choices.get(position));
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public interface CheckAnswerListener {
        void checkAnswer(View view);
    }

    public void setOnCheckAnswerListener(CheckAnswerListener listener) {
        this.mListener = listener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.choice);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.checkAnswer(v);
            } else {
                Log.d(TAG, "onClick: mListener is null");
            }
        }
    }

}
