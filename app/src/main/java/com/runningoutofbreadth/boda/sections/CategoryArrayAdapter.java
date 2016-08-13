package com.runningoutofbreadth.boda.sections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.FlashcardActivity;

import java.util.ArrayList;

/**
 * Created by SandD on 7/30/2016.
 */
public class CategoryArrayAdapter extends RecyclerView.Adapter<CategoryArrayAdapter.ViewHolder> {

    private static final String LOG_TAG = CategoryArrayAdapter.class.getSimpleName();
    private static final String PREFS_FILENAME = "BodaPrefsFile";

    private ArrayList<String> mArrayList;
    private Context mContext;
    private int mRowLayoutResId;
    private Class mActivityToStart;
    private boolean hasOverlay;

    public CategoryArrayAdapter(Context mContext, ArrayList<String> mArrayList, int mRowLayoutResId, Class mActivityToStart) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mRowLayoutResId = mRowLayoutResId;
        this.mActivityToStart = mActivityToStart;
    }

    public CategoryArrayAdapter(Context mContext, ArrayList<String> mArrayList, int mRowLayoutResId, Class mActivityToStart, boolean hasOverlay) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mRowLayoutResId = mRowLayoutResId;
        this.mActivityToStart = mActivityToStart;
        this.hasOverlay = hasOverlay;
    }

    // Reference to all views for each list item to make a reusable view
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            ViewTreeObserver.OnGlobalLayoutListener {
        ImageView imageView;
        TextView textView;
        String category;
        View overlayView;
//        int imageResourceId;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.category_badge_imageview);
            textView = (TextView) itemView.findViewById(R.id.category_textview);
            // extra logic for the profile badge revealer
            if (hasOverlay) {
                overlayView = itemView.findViewById(R.id.overlay);
                ViewTreeObserver overlayViewViewTreeObserver = overlayView.getViewTreeObserver();
                overlayViewViewTreeObserver.addOnGlobalLayoutListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mActivityToStart != null) {
                Intent intent = new Intent(mContext, mActivityToStart);
                intent.putExtra(FlashcardActivity.CATEGORY, category);
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onGlobalLayout() {
            if (hasOverlay) {
                // take percentage read from shared prefs, use to modify height of overlay
                String CATEGORY_KEY = category.toUpperCase() + "_READ_COUNT";
                SharedPreferences settings = mContext.getSharedPreferences(PREFS_FILENAME, 0);
                double modifier = 1 - Double.longBitsToDouble(settings.getLong(CATEGORY_KEY, 0));

                // once view has been measured, get current params and modify accordingly
                ViewTreeObserver overlayViewTreeObserver = overlayView.getViewTreeObserver();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) overlayView.getLayoutParams();
                params.height = Double.valueOf(overlayView.getHeight() * modifier).intValue();

                overlayView.setLayoutParams(params);

                // remove or else infinite loop from constantly changing layout
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    overlayViewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    overlayViewTreeObserver.removeGlobalOnLayoutListener(this);
                }

            }
        }
    }

    @Override
    public CategoryArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(mRowLayoutResId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.category = mArrayList.get(position);
        // TODO: 8/12/2016 refactor to pass in id's instead of ugly string parsing
        int imageResId = mContext.getResources().getIdentifier(
                "ic_" + mArrayList.get(position).toLowerCase(),
                "drawable",
                mContext.getPackageName()
        );
        holder.textView.setText(holder.category);
        Glide.with(mContext)
                .load(imageResId)
                .error(android.R.drawable.btn_star_big_on)
                .into(holder.imageView);
    }


//    @Override
//    public void onViewAttachedToWindow(ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        if (hasOverlay){
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.overlayView.getLayoutParams();
//            params.height = Double.valueOf(holder.overlayView.getMeasuredHeight() * 0.8).intValue();
//            Log.v(LOG_TAG, Integer.toString(holder.overlayView.getMeasuredHeight()) +
//                    " is the original height in pixels");
//            Log.v(LOG_TAG, Double.valueOf((holder.overlayView.getHeight() * 0.8)).intValue() +
//                    " is the new height in pixels");
//
//            holder.overlayView.setLayoutParams(params);
//        }
//    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

}

