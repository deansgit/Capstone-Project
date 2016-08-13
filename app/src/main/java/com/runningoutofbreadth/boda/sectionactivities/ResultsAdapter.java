package com.runningoutofbreadth.boda.sectionactivities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;

import java.util.ArrayList;

/**
 * Created by SandD on 8/11/2016.
 */
public class ResultsAdapter extends ArrayAdapter<ColoredResult> {

    private static final String LOG_TAG = ResultsAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<ColoredResult> mResultsList;
    private int mResource;

    public ResultsAdapter(Context context, int resource, ArrayList<ColoredResult> resultsList) {
        super(context, resource, resultsList);
        mContext = context;
        mResultsList = resultsList;
        mResource = resource;
    }

    private class ViewHolder {
        int mColorResId;
        TextView hangeulTextView;
        TextView romanizationTextView;
        TextView translationTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ColoredResult currentResult = mResultsList.get(position);
        viewHolder.hangeulTextView = (TextView) convertView.findViewById(R.id.hangeul);
        viewHolder.romanizationTextView = (TextView) convertView.findViewById(R.id.romanization);
        viewHolder.translationTextView = (TextView) convertView.findViewById(R.id.translation);
        viewHolder.mColorResId = currentResult.colorResId;

        viewHolder.hangeulTextView.setText(currentResult.hangeul);
        viewHolder.romanizationTextView.setText(currentResult.romanization);
        viewHolder.translationTextView.setText(currentResult.translation);

        viewHolder.hangeulTextView.setTextColor(ContextCompat.getColor(mContext, viewHolder.mColorResId));
        viewHolder.romanizationTextView.setTextColor(ContextCompat.getColor(mContext, viewHolder.mColorResId));
        viewHolder.translationTextView.setTextColor(ContextCompat.getColor(mContext, viewHolder.mColorResId));

        return convertView;
    }

}

