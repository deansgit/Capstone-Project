package com.runningoutofbreadth.boda.sections;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.runningoutofbreadth.boda.R;
import com.runningoutofbreadth.boda.sectionactivities.SpeedReaderActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpeedReaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeedReaderFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = SpeedReaderFragment.class.getSimpleName();
    private static final String PREFS_FILENAME = "BodaPrefsFile";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SYLLABLE_COUNT = "COUNT";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSlowButton;
    private Button mNormalButton;
    private Button mFastButton;
    private Button mLudicrousButton;

    public static final long DELAY_SLOW = 3000;
    public static final long DELAY_NORMAL = 2000;
    public static final long DELAY_FAST = 1000;
    public static final long DELAY_LUDICROUS = 500;
    private EditText mSyllableCountEditText;

    int mTotalCount;

    public SpeedReaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeedReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeedReaderFragment newInstance(String param1, String param2) {
        SpeedReaderFragment fragment = new SpeedReaderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_speed_reader, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //help stop the blinking?
            getActivity().getWindow().setExitTransition(null);
            getActivity().getWindow().setEnterTransition(null);
            getActivity().getWindow().setReenterTransition(null);
            getActivity().getWindow().setReturnTransition(null);
        }

        mSyllableCountEditText = (EditText) rootView.findViewById(R.id.syllable_count_input);
        mSyllableCountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.v(LOG_TAG, Boolean.toString(v.getId() == mSyllableCountEditText.getId())
                            + ", edittext not focused");
                    mSyllableCountEditText.setCursorVisible(false);
                } else {
                    Log.v(LOG_TAG, Boolean.toString(v.getId() == mSyllableCountEditText.getId())
                            + ", edittext focused");
                    mSyllableCountEditText.setCursorVisible(true);
                }
            }
        });

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_FILENAME, 0);

        mSyllableCountEditText.setText(String.valueOf(settings.getInt(SYLLABLE_COUNT, 10)));

        mSlowButton = (Button) rootView.findViewById(R.id.slow);
        mNormalButton = (Button) rootView.findViewById(R.id.normal);
        mFastButton = (Button) rootView.findViewById(R.id.fast);
        mLudicrousButton = (Button) rootView.findViewById(R.id.ludicrous);

        mSlowButton.setOnClickListener(this);
        mNormalButton.setOnClickListener(this);
        mFastButton.setOnClickListener(this);
        mLudicrousButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getActivity(), SpeedReaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //allows back button on next activity to go home
        mTotalCount = !mSyllableCountEditText.getText().toString().equals("")
                ? Integer.decode(mSyllableCountEditText.getText().toString()) : 10;
        intent.putExtra(SpeedReaderActivity.SYLLABLE_COUNT, mTotalCount);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                v.getRootView().findViewById(R.id.syllable_count_input), getString(R.string.syllable_count_transition_name));

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SYLLABLE_COUNT, mTotalCount);
        editor.apply();

        switch (id) {
            case R.id.slow:
                intent.putExtra(SpeedReaderActivity.DIFFICULTY, DELAY_SLOW);
                break;
            case R.id.normal:
                intent.putExtra(SpeedReaderActivity.DIFFICULTY, DELAY_NORMAL);
                break;
            case R.id.fast:
                intent.putExtra(SpeedReaderActivity.DIFFICULTY, DELAY_FAST);
                break;
            case R.id.ludicrous:
                intent.putExtra(SpeedReaderActivity.DIFFICULTY, DELAY_LUDICROUS);
                break;
        }
        ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSyllableCountEditText != null && mSyllableCountEditText.hasFocus()) {
            mSyllableCountEditText.clearFocus();
        }
    }
}
