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
    public static String wordSelector(final int position, Class model) {
        // final String output for test
        String word = "";
        // return generic model class (Animal, Syllable, Nation, etc.)
        Model unicodeString = null;
        Method getHangeul = null;
        Method getsID = null;
        try {
            // Create a 'Condition' for DBFlow to use in the SQLite query's WHERE clause
            String[] elements;
            NameAlias alias = NameAlias.builder("sId").build();
            Condition randomIdCondition = Condition.column(alias).eq(position);
            try {
                // get string: format should be hex values with spaces e.g. "XXXX XXXX XXXX"
                unicodeString = SQLite.select()
                        .from(model)
                        .where(randomIdCondition)
                        .querySingle();
                if (unicodeString != null) {
                    getHangeul = unicodeString.getClass().getMethod("getHangeul");
                    getsID = unicodeString.getClass().getMethod("getsId");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Log.v(LOG_TAG, getHangeul.invoke(unicodeString, null).toString() + " invokemethod");
//            Log.v(LOG_TAG, getsID.invoke(unicodeString, null).toString() + " versus " + position);
            if (getHangeul != null) {
                elements = getHangeul.invoke(unicodeString).toString().split(" ");
                for (String s : elements) {
                    word = word + String.valueOf((char) Integer.parseInt(s, 16));
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return word;
    }

    /**
     * Returns a different typeface from the assets folder.
     */
    public static Typeface diffFont(Context context){
        int fontIndex = randInt(0, FONT_MAP.length - 1); // index always off by one from array length
        return Typeface.createFromAsset(context.getAssets(), FONT_MAP[fontIndex]);
    }

}
