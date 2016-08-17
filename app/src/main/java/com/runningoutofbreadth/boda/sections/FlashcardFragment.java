package com.runningoutofbreadth.boda.sections;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.FlashcardActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardFragment extends Fragment implements RecyclerView.OnClickListener {
    private static final String LOG_TAG = FlashcardFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORIES = "CATEGORIES";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mCategoryArray;


    public FlashcardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryArray Parameter 1.
     * @return A new instance of fragment FlashcardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlashcardFragment newInstance(ArrayList<String> categoryArray) {
        FlashcardFragment fragment = new FlashcardFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_flashcard, container, false);

        RecyclerView catRecView = (RecyclerView) rootView.findViewById(R.id.category_badges_gridview);
        catRecView.setHasFixedSize(true);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        catRecView.setLayoutManager(gridLayoutManager);

        CategoryArrayAdapter catArrAdapter = new CategoryArrayAdapter(getActivity(),
                mCategoryArray, R.layout.category_list_item, FlashcardActivity.class);
        catRecView.setAdapter(catArrAdapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        TextView referenceView = (TextView) v.findViewById(R.id.category_textview);
        String category = referenceView.getText().toString();
        Intent intent = new Intent(getActivity(), FlashcardActivity.class);
        intent.putExtra(FlashcardActivity.CATEGORY, category);
        startActivity(intent);
    }

}
