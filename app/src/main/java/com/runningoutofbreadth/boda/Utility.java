package com.runningoutofbreadth.boda;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.runningoutofbreadth.boda.db.Word;
import com.runningoutofbreadth.boda.db.WordFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SandD on 7/1/2016.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static final String[] FONT_MAP = {
            "fonts/210sulamjuui_bold_handwritten.ttf",
            "fonts/210sulamjuui_light_handwritten.ttf",
            "fonts/210sulamjuui_reg_handwritten.ttf",
            "fonts/cuteshinminsang_handwritten.ttf",
            "fonts/darae_handwritten.ttf",
            "fonts/hamchorongbatang_typed.ttf",
            "fonts/hoonwhitecat_handwritten.ttf",
            "fonts/nanumpen_handwritten.ttf",
            "fonts/oseunghaneum_bold_typed.ttf"
    };

    // add some kind of nullable param in case there is no image data.
    public static void insertDatabaseObjects(DatabaseWrapper databaseWrapper,
                                             AssetManager assetManager, String assetPath, Class model) {
        try {
            InputStream dictionary = assetManager.open(assetPath);
            ArrayList<String> mList = dictReader(dictionary);
            String[] tokens;
            Word dbObject;
            for (int i = 0; i < mList.size(); i++) {
                tokens = mList.get(i).split(",");
                try {
                    dbObject = WordFactory.getWord(model);
                    dbObject.setsId(i);
                    dbObject.setName(tokens[0]);
                    dbObject.setHangeul(tokens[1]);
                    dbObject.setRomanization(tokens[2]);
                    if (tokens.length > 3) {
                        dbObject.setImageId(tokens[3]);
                    }
                    dbObject.save(databaseWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dictionary.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for iterating through each line in a file.
     */
    public static ArrayList<String> dictReader(InputStream inputStream) {
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
     * Dynamically select a database item based on the type of data (model) requested
     */
    public static String[] wordSelector(final int position, Class model) {
        // final String output for test
        String word = "";
        String romanization = "";
        String imageId = "";
        // return generic model class (Animal, Syllable, Nation, etc.)
        Model wordItem = null;
        Method getHangeul = null;
        Method getRomanization = null;
        Method getImageId = null;
        try {
            // Create a 'Condition' for DBFlow to use in the SQLite query's WHERE clause

            NameAlias alias = NameAlias.builder("sId").build();
            Condition randomIdCondition = Condition.column(alias).eq(position);
            try {
                // get string: format should be hex values with spaces e.g. "XXXX XXXX XXXX"
                wordItem = SQLite.select()
                        .from(model)
                        .where(randomIdCondition)
                        .querySingle();
                if (wordItem != null) {
                    getHangeul = wordItem.getClass().getMethod("getHangeul");
                    getRomanization = wordItem.getClass().getMethod("getRomanization");
                    getImageId = wordItem.getClass().getMethod("getImageId");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Log.v(LOG_TAG, getHangeul.invoke(wordItem, null).toString() + " invokemethod");
//            Log.v(LOG_TAG, getRomanization.invoke(wordItem, null).toString() + " versus " + position);
//            Log.v(LOG_TAG, getImageId.invoke(wordItem, null).toString() + " versus " + position);
            if (getHangeul != null && getRomanization != null) {
                String[] elements = getHangeul.invoke(wordItem).toString().split(" ");
                for (String s : elements) {
                    word = word + String.valueOf((char) Integer.parseInt(s, 16));
                }
                romanization = getRomanization.invoke(wordItem).toString();
            }
            if (getImageId != null){
                imageId = (String) getImageId.invoke(wordItem);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (new String[]{word, romanization, imageId});
    }

    /**
     * Returns a different typeface from the assets folder.
     */
    //// TODO: 7/16/2016 make sure it returns a different number from previous
    public static Typeface diffFont(Context context){
        int fontIndex = randInt(0, FONT_MAP.length - 1); // index always off by one from array length
        return Typeface.createFromAsset(context.getAssets(), FONT_MAP[fontIndex]);
    }

}
