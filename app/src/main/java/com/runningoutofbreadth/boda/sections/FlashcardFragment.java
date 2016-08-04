package com.runningoutofbreadth.boda.sections;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.FlashcardActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = FlashcardFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORIES = "CATEGORIES";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mCategoryArray;
    private String mParam2;


    public FlashcardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryArray Parameter 1.
     * @param param2        Parameter 2.
     * @return A new instance of fragment FlashcardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlashcardFragment newInstance(ArrayList<String> categoryArray, String param2) {
        FlashcardFragment fragment = new FlashcardFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_flashcard, container, false);

        CategoryArrayAdapter categoryArrayAdapter = new CategoryArrayAdapter(getContext(),
                R.layout.category_list_item, mCategoryArray);

        GridView categoryGridView = (GridView) rootView.findViewById(R.id.category_badges_gridview);
        categoryGridView.setAdapter(categoryArrayAdapter);
        categoryGridView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView referenceView = (TextView) view.findViewById(R.id.category_textview);
        String category = referenceView.getText().toString();
        Intent intent = new Intent(getActivity(), FlashcardActivity.class);
        intent.putExtra(FlashcardActivity.CATEGORY, category);
        startActivity(intent);
    }
}
