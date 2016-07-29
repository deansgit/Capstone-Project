package com.runningoutofbreadth.boda.sections;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runningoutofbreadth.boda.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORIES = "CATEGORIES";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam2;

    private ArrayList<String> mCategoryArray;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(ArrayList<String> param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CATEGORIES, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryArray = getArguments().getStringArrayList(CATEGORIES);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        CategoryArrayAdapter categoryArrayAdapter = new CategoryArrayAdapter(getContext(),
                R.layout.category_list_item, mCategoryArray);

        GridView gridView = (GridView) rootView.findViewById(R.id.category_badges_gridview);
        gridView.setAdapter(categoryArrayAdapter);

        return rootView;
    }

    public class CategoryArrayAdapter extends ArrayAdapter {

        public CategoryArrayAdapter(Context context, int resource, ArrayList arrayList) {
            super(context, resource, arrayList);
        }

        private class ViewHolder {
            ImageView imageView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            Log.v(LOG_TAG, parent.getClass().getName() + " is the parent");
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.category_list_item, parent, false);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.category_badge_imageview);
            Glide.with(getContext())
                    .load(getImageResId(mCategoryArray.get(position)))
                    .error(android.R.drawable.btn_star_big_on)
                    .into(viewHolder.imageView);
            return convertView;
        }

        private int getImageResId(String category) {
            try {
                Log.v(LOG_TAG, Integer.toString(getResources().getIdentifier(category, "drawables", getContext().getPackageName())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getResources().getIdentifier("badge_" + category, "drawables", getContext().getPackageName());
        }
    }

}
