package com.runningoutofbreadth.boda;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.runningoutofbreadth.boda.db.BodaDatabase;
import com.runningoutofbreadth.boda.sections.FlashcardFragment;
import com.runningoutofbreadth.boda.sections.ProfileFragment;
import com.runningoutofbreadth.boda.sections.QuizFragment;
import com.runningoutofbreadth.boda.sections.SpeedReaderFragment;

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

    private final String[][] ASSET_REFERENCES = {
            {"dictionaries/syllables_dict.txt", "Syllable"},
            {"dictionaries/animals_dict.txt", "Animal"},
            {"dictionaries/nations_dict.txt", "Nation"},
            {"dictionaries/idioms_dict.txt", "Idiom"},
            {"dictionaries/vocabulary_dict.txt", "Vocabulary"}
    };
    private static final int DICTIONARY_PATH_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;

    // boolean for checking sharedPref to see if database was already loaded
    private static boolean mDBUpdated;
    private static final String DB_UPDATE_STATUS = "DB_UPDATE_STATUS";
    private static final String DB_VERSION_NUMBER = "DB_VERSION_NUMBER";

    // holds all of the categories to be passed into each fragment (predom. for listview population)
    private ArrayList<String> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // TODO: 7/29/2016 make profile tab smaller
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        mCategories = new ArrayList<>();
        for (int i = 0; i < ASSET_REFERENCES.length; i++) {
            mCategories.add(ASSET_REFERENCES[i][CATEGORY_INDEX]);
        }

        // load database for the first time
        SharedPreferences settings = getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        mDBUpdated = settings.getBoolean(DB_UPDATE_STATUS, false);
        if (!settings.contains(DB_VERSION_NUMBER) || settings.getInt(DB_VERSION_NUMBER, 0) != BodaDatabase.VERSION) {
            Log.v(LOG_TAG, Integer.toString(settings.getInt(DB_VERSION_NUMBER, 0)));
            editor.putInt(DB_VERSION_NUMBER, BodaDatabase.VERSION);
            mDBUpdated = false;
        }
        Log.d(LOG_TAG, Boolean.toString(mDBUpdated));
        if (!mDBUpdated) {
            DatabaseDefinition databaseDefinition = FlowManager.getDatabase(BodaDatabase.class);
            Transaction transaction = databaseDefinition.beginTransactionAsync(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
                    Log.d(LOG_TAG, "execute for database-async-transaction called");
                    AssetManager assetManager = getApplicationContext().getAssets();
                    try {
                        for (String[] path : ASSET_REFERENCES) {
                            Class model = Class.forName(getPackageName() + ".db." + path[CATEGORY_INDEX]);
                            Utility.insertDatabaseObjects(databaseWrapper, assetManager, path[DICTIONARY_PATH_INDEX], model);
                            if (SQLite.selectCountOf().from(model).count() == 0) {
                                mDBUpdated = false;
                                break;
                            } else {
                                mDBUpdated = true;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "string is broken");
                        e.printStackTrace();
                        mDBUpdated = false;
                    }
                }
            }).build();
            transaction.execute();
        }
//        Log.v(LOG_TAG, SQLite.select().from(Syllable.class).where(Syllable_Table.sId.eq(5)).querySingle().getTranslation() + " new");
//        Log.v(LOG_TAG, SQLite.select().from(Animal.class).where(Animal_Table.sId.eq(5)).querySingle().getTranslation() + " new");
//        Log.v(LOG_TAG, SQLite.select().from(Nation.class).where(Nation_Table.sId.eq(5)).querySingle().getTranslation() + " new");
//        Log.v(LOG_TAG, SQLite.select().from(Idiom.class).where(Idiom_Table.sId.eq(5)).querySingle().getTranslation() + " new");
//        Log.v(LOG_TAG, SQLite.select().from(Vocabulary.class).where(Vocabulary_Table.sId.eq(5)).querySingle().getTranslation() + " new");
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        // if setting doesn't exist or if the version number has changed, update number and set bool
        // to false to kickstart db updating.
        if (!settings.contains(DB_VERSION_NUMBER) || settings.getInt(DB_VERSION_NUMBER, 0) != BodaDatabase.VERSION) {
            Log.v(LOG_TAG, Integer.toString(settings.getInt(DB_VERSION_NUMBER, 0)));
            editor.putInt(DB_VERSION_NUMBER, BodaDatabase.VERSION);
            mDBUpdated = false;
        }
        editor.putBoolean(DB_UPDATE_STATUS, mDBUpdated);
        editor.commit();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int FLASHCARDS = 0;
        private static final int QUIZ = 1;
        private static final int SPEEDREAD = 2;
        private static final int PROFILE = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            switch (position) {
                case FLASHCARDS:
                    fragment = FlashcardFragment.newInstance(mCategories);
                    break;
                case QUIZ:
                    fragment = QuizFragment.newInstance(mCategories);
                    break;
                case SPEEDREAD:
                    fragment = SpeedReaderFragment.newInstance();
                    break;
                case PROFILE:
                    fragment = ProfileFragment.newInstance(mCategories);
                    break;
                default:
                    throw new RuntimeException("Can't create fragment. Number doesn't exist.");
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FLASH CARDS";
                case 1:
                    return "QUIZ";
                case 2:
                    return "SPEED READ";
                case 3:
                    return "PROFILE";
            }
            return null;
        }
    }

}
