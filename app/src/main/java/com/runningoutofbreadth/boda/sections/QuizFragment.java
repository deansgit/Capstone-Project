package com.runningoutofbreadth.boda.sections;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.QuizActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment implements RecyclerView.OnClickListener {
    private static final String LOG_TAG = QuizFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORIES = "CATEGORIES";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mCategoryArray;
    private String mParam2;


    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryArray Parameter 1.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(ArrayList<String> categoryArray) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CATEGORIES, categoryArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryArray = getArguments().getStringArrayList(CATEGORIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        RecyclerView catRecView = (RecyclerView) rootView.findViewById(R.id.category_badges_gridview);
        catRecView.setHasFixedSize(true);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        catRecView.setLayoutManager(gridLayoutManager);

        CategoryArrayAdapter catArrAdapter = new CategoryArrayAdapter(getActivity(),
                mCategoryArray, R.layout.category_list_item, QuizActivity.class);
        catRecView.setAdapter(catArrAdapter);
        catRecView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.v(LOG_TAG, Integer.toString(v.getId()));
        TextView referenceView = (TextView) v.findViewById(R.id.category_textview);
        String category = referenceView.getText().toString();
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra(QuizActivity.CATEGORY, category);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //allows back button on next activity to go home
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityCompat.startActivity(getActivity(), intent, null);
    }
}
