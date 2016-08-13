package com.runningoutofbreadth.boda.sections;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
     * @param categoryArray Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(ArrayList<String> categoryArray, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CATEGORIES, categoryArray);
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

        RecyclerView catRecView = (RecyclerView) rootView.findViewById(R.id.category_badges_gridview);
        catRecView.setHasFixedSize(true);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        catRecView.setLayoutManager(gridLayoutManager);

        CategoryArrayAdapter catArrAdapter = new CategoryArrayAdapter(getActivity(),
                mCategoryArray, R.layout.profile_badge_list_item, null, true);
        catRecView.setAdapter(catArrAdapter);

        return rootView;
    }

}
