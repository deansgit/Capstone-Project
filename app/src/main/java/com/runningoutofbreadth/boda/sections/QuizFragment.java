package com.runningoutofbreadth.boda.sections;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.QuizActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        ImageButton animalsImgBtn = (ImageButton) rootView.findViewById(R.id.animals);
        ImageButton syllablesImgBtn = (ImageButton) rootView.findViewById(R.id.syllables);
        ImageButton nationsImgBtn = (ImageButton) rootView.findViewById(R.id.nations);
        ImageButton idiomsImgBtn = (ImageButton) rootView.findViewById(R.id.idioms);

        animalsImgBtn.setOnClickListener(this);
        syllablesImgBtn.setOnClickListener(this);
        nationsImgBtn.setOnClickListener(this);
        idiomsImgBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        switch (id){
            case R.id.animals:
                intent.putExtra(QuizActivity.CATEGORY, QuizActivity.CATEGORY_ANIMALS);
                break;
            case R.id.syllables:
                intent.putExtra(QuizActivity.CATEGORY, QuizActivity.CATEGORY_SYLLABLES);
                break;
            case R.id.nations:
                intent.putExtra(QuizActivity.CATEGORY, QuizActivity.CATEGORY_NATIONS);
                break;
            case R.id.idioms:
                intent.putExtra(QuizActivity.CATEGORY, QuizActivity.CATEGORY_IDIOMS);
                break;
        }
        startActivity(intent);
    }
}
