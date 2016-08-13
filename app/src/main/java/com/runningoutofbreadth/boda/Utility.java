package com.runningoutofbreadth.boda;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.runningoutofbreadth.boda.db.Syllable;
import com.runningoutofbreadth.boda.db.Word;
import com.runningoutofbreadth.boda.db.WordFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Collection of useful methods for parsing strings, retrieving data from tables, and some basic
 * algorithms and calculations.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static final String[] FONT_MAP = {
            "fonts/210sulamjuui_reg_handwritten.ttf",
            "fonts/cuteshinminsang_handwritten.ttf",
            "fonts/darae_handwritten.ttf",
            "fonts/hamchorongbatang_typed.ttf",
            "fonts/hoonwhitecat_handwritten.ttf",
            "fonts/nanumpen_handwritten.ttf",
            "fonts/oseunghaneum_bold_typed.ttf"
    };

    public static void insertDatabaseObjects(DatabaseWrapper databaseWrapper,
                                             AssetManager assetManager, String assetPath, Class category) {
        try {
            InputStream dictionary = assetManager.open(assetPath);
            ArrayList<String> mList = dictReader(dictionary);
            String[] tokens;
            Word dbObject;
            for (int i = 0; i < mList.size(); i++) {
                tokens = mList.get(i).split(",");
                try {
                    dbObject = WordFactory.build(category);
                    dbObject.setsId(i);
                    dbObject.setTranslation(tokens[0]);
                    dbObject.setHangeul(tokens[1]);
                    dbObject.setRomanization(tokens[2]);
                    dbObject.setRead(false);
                    if (tokens.length > 3) {
                        dbObject.setImageId(tokens[3]);
                    }
                    dbObject.save(databaseWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            dictionary.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for iterating through each line in a file.
     * Only used with InsertDatabaseObjects.
     */
    private static ArrayList<String> dictReader(InputStream inputStream) {
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

    /**
     * Randomizer predominantly used with wordSelector to spit out random words in range
     */
    public static int randInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * Dynamically select a database item based on the category (category) requested
     */
    public static Word wordSelector(final int position, Class category) {
        Word newWord;

        // final String output for test
        String translation = "";
        String hangeul = "";
        String romanization = "";
        String imageId;
        boolean read = true;
        // return generic category class (Animal, Syllable, Nation, etc.)
        Model wordItem = null;
        Method getTranslation = null;
        Method getHangeul = null;
        Method getRomanization = null;
        Method getImageId = null;
        try {
            // Create a 'Condition' for DBFlow to use in the SQLite query's WHERE clause
            newWord = (Word) category.newInstance();
            NameAlias sIDAlias = NameAlias.builder("sId").build();
            Condition randomIdCondition = Condition.column(sIDAlias).eq(position);
            try {
                // get string: format should be hex values with spaces e.g. "XXXX XXXX XXXX"
                wordItem = SQLite.select()
                        .from(category)
                        .where(randomIdCondition)
                        .querySingle();
                if (wordItem != null) {
                    getTranslation = wordItem.getClass().getMethod("getTranslation");
                    getHangeul = wordItem.getClass().getMethod("getHangeul");
                    getRomanization = wordItem.getClass().getMethod("getRomanization");
                    getImageId = wordItem.getClass().getMethod("getImageId");

                    NameAlias readAlias = NameAlias.builder("read").build();
                    Condition readCondition = Condition.column(readAlias).eq(true);

                    SQLite.update(category)
                            .set(readCondition)
                            .where(randomIdCondition)
                            .async()
                            .execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getTranslation != null && getHangeul != null && getRomanization != null) {
                String[] unicode = getHangeul.invoke(wordItem).toString().split(" ");
                for (String s : unicode) {
                    hangeul = hangeul + String.valueOf((char) Integer.parseInt(s, 16));
                }
                translation = getTranslation.invoke(wordItem).toString();
                romanization = getRomanization.invoke(wordItem).toString();
            }
            if (getImageId != null) {
                imageId = (String) getImageId.invoke(wordItem);
                newWord.setImageId(imageId);
            }
            newWord.setsId(position);
            newWord.setTranslation(translation);
            newWord.setHangeul(hangeul);
            newWord.setRomanization(romanization);
            newWord.setRead(true);

            return newWord;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a different typeface from the assets folder.
     */
    //// TODO: 7/16/2016 make sure it returns a different number from previous
    public static Typeface diffFont(Context context) {
        int fontIndex = randInt(0, FONT_MAP.length - 1); // index always off by one from array length
        return Typeface.createFromAsset(context.getAssets(), FONT_MAP[fontIndex]);
    }

    /**
     * Generate a random syllable
     **/
    public static Word randomSyllable() {
        int randPosMax = (int) SQLite.selectCountOf().from(Syllable.class).count();
        return Utility.wordSelector(Utility.randInt(0, randPosMax), Syllable.class);
    }

    /**
     * Generate a specific syllable by id
     **/
    public static Word queryWordById(int id, Class category) {
        return Utility.wordSelector(id, category);
    }

    /**
     * Helper image loader
     **/
    public static void glideLoadImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId).error(android.R.drawable.picture_frame)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Generates list of n random non-repeating ids in range
     **/
    public static HashSet<Integer> generateListOfIds(Class category, int counter) {
        HashSet<Integer> set = new HashSet<>();
        int randPosMax = (int) SQLite.selectCountOf().from(category).count();

        while (set.size() < counter) {
            set.add(randInt(0, randPosMax));
        }
        return set;
    }

    /**
     * Convenience method for iterating through the Set of table IDs
     * to retrieve the Word items.
     */
    public static List<Word> generateWordList(HashSet<Integer> idList, Class category) {
        List<Word> wordList = new ArrayList<>();
        for (int id : idList) {
            wordList.add(Utility.queryWordById(id, category));
        }
        return wordList;
    }

    /**
     * Simple convenience method to slowly fade-in View
     **/
    public static void slowFadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(2000)
                .setListener(null);
    }

    /**
     * Makes a view grow momentarily. Mainly used to alert user something significant has changed.
     **/
    public static void animatePulse(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.grow);
        animation.setDuration(1000);
        view.startAnimation(animation);
    }

    /**
     * Persistent read/unread counter
     **/
    public static void writeProgressToPrefs(SharedPreferences.Editor editor, Class category) {
        NameAlias alias = NameAlias.builder("read").build();
        Condition readCondition = Condition.column(alias).eq(true);
        try {
            // total number of rows in table for CATEGORY_TABLE
            long totalCount = SQLite.selectCountOf().from(category).count();
            // total number of items already marked as "read/true"
            long readCount = SQLite.selectCountOf().from(category).where(readCondition).count();
            // percent progress to be used to modify size of alpha-overlay on profile badge
            double percentageRead = readCount * 1.0 / totalCount;
            // save to prefs
            editor.putLong(category.getSimpleName().toUpperCase() + "_READ_COUNT",
                    Double.doubleToRawLongBits(percentageRead));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convenience method for changing the read/unread database value for a word
     **/
    public static void markAsRead(Word word, boolean isRead) {
        // get ref to a Class/Model object for DBFlow
        Class category = word.getClass();

        // create reference to each column in db Table
        NameAlias sIDAlias = NameAlias.builder("sId").build();
        NameAlias readAlias = NameAlias.builder("read").build();

        // create 'WHERE' modifiers
        Condition randomIdCondition = Condition.column(sIDAlias).eq(word.getsId());
        Condition readCondition = Condition.column(readAlias).eq(isRead);

        // change "read (true/false)" value of db row that matches with current word's id
        SQLite.update(category)
                .set(readCondition)
                .where(randomIdCondition)
                .async()
                .execute();
    }

}
