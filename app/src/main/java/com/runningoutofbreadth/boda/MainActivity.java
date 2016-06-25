package com.runningoutofbreadth.boda;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_FILENAME = "BodaPrefsFile";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // TODO: 6/13/2016 add string keys and map out to fonts' paths
    ArrayList<String> mList;

    // boolean for checking sharedPref to see if database was already loaded
    private static boolean mDBUpdated;
    private static final String DB_UPDATE_STATUS = "DB_UPDATE_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load database for the first time
        // TODO: add logic to check for changes in db. if not changed, do not do this.
        SharedPreferences settings = getSharedPreferences(PREFS_FILENAME, 0);
        mDBUpdated = settings.getBoolean(DB_UPDATE_STATUS, false);
        if (!mDBUpdated) {
            DatabaseDefinition databaseDefinition = FlowManager.getDatabase(BodaDatabase.class);
            Transaction transaction = databaseDefinition.beginTransactionAsync(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
//                Log.v(LOG_TAG, "execute for database-async-transaction called");
                    AssetManager assetManager = getApplicationContext().getAssets();
                    try {
                        InputStream dictionary = assetManager.open("dictionaries/testdict.txt");
                        mList = dictReader(dictionary);
                        for (int i = 0; i <= mList.size(); i++) {
                            String[] tokens = mList.get(i).split(",");
                            Syllable syllable = new Syllable();
                            syllable.setsId(i);
                            syllable.setUnicode_name(tokens[0]);
                            syllable.setSyllable(tokens[1]);
                            syllable.setRomanization(tokens[2]);
                            syllable.save(databaseWrapper);
                        Log.v(LOG_TAG, syllable.getSyllable());
                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "string is broken");
                    }
                }
            }).build();
            transaction.execute();
            mDBUpdated = true;
        }
        Log.v(LOG_TAG, SQLite.select().from(Syllable.class).where(Syllable_Table.sId.eq(5)).querySingle().toString() + "new");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Typeface darae = Typeface
                    .createFromAsset(getActivity().getAssets(), "fonts/darae_handwritten.ttf");
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setTypeface(darae);
            textView.setText("다래");

            // todo: generate random number between 1 and dbtable.size, return int into SQLite.where clause

            try {
                //db items should have format ==> [sId, unicode_name, hex, romanization]
                int hex = Integer.parseInt(SQLite.select().from(Syllable.class).where(Syllable_Table.sId.eq(6)).querySingle().getSyllable(), 16);
                textView.setText(String.valueOf((char) hex));

            } catch (Exception e) {
                Log.e(LOG_TAG, "string is broken");
            }
//            textView.setText(testLine);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onStop() {
            super.onStop();

            SharedPreferences settings = getActivity().getSharedPreferences(PREFS_FILENAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(DB_UPDATE_STATUS, mDBUpdated);
            editor.commit();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }


    /**
    * Helper method for iterating through each line in a file.
    */
    public ArrayList<String> dictReader(InputStream inputStream) {
        ArrayList<String> stringArray = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    stringArray.add(line);
//                    Log.e(LOG_TAG, line);
                }
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e(LOG_TAG, "IO error for StringBuilder");
            } finally {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e(LOG_TAG, "file not found");
        }
        return stringArray;
    }
}
