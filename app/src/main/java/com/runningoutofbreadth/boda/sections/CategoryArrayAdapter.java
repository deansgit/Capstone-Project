package com.runningoutofbreadth.boda.sections;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.runningoutofbreadth.boda.R;

import java.util.ArrayList;

/**
 * Created by SandD on 7/30/2016.
 */
public class CategoryArrayAdapter extends ArrayAdapter{

    private ArrayList<String> mArrayList;
    private Context mContext;
    private int mResource;

    public CategoryArrayAdapter(Context context, int resource, ArrayList<String> arrayList) {
        super(context, resource, arrayList);
        mArrayList = arrayList;
        mContext = context;
        mResource = resource;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
        int imageResourceId;

        public int getImageResourceId() {
            return imageResourceId;
        }

        public void setImageResourceId(int imageResourceId) {
            this.imageResourceId = imageResourceId;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            Log.v(LOG_TAG, parent.getClass().getName() + " is the parent");
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String category = mArrayList.get(position);
        viewHolder.setImageResourceId(getImageResId(category));
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.category_badge_imageview);

        Glide.with(getContext())
                .load(viewHolder.getImageResourceId())
                .error(android.R.drawable.btn_star_big_on)
                .into(viewHolder.imageView);

        viewHolder.textView = (TextView) convertView.findViewById(R.id.category_textview);
        viewHolder.textView.setText(category);
        return convertView;
    }

    private int getImageResId(String category) {
        return mContext.getResources().getIdentifier("ic_" + category.toLowerCase(), "drawable", getContext().getPackageName());
    }
}

