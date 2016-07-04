package com.runningoutofbreadth.boda;

import android.content.res.AssetManager;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SandD on 7/1/2016.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

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
     * Randomizer
     */
    public static int randInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * Dynamically select a database item based on the type of data (model) requested
     */
    @SuppressWarnings("ConstantConditions")
    public static String wordSelector(final int position, Class model) {
//        String tableName = model.getName() + "_Table";
        // get reference to table.
        String word = "";
        try {
            NameAlias alias = NameAlias.builder("sId").build();
            Condition randomIdCondition = Condition.column(alias).eq(position);
            Model unicodeString = null;
            try {
                // get string: format should be hex values with spaces e.g. "XXXX XXXX XXXX"
                unicodeString = SQLite.select()
                        .from(model)
                        .where(randomIdCondition)
                        .querySingle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Method getHangeul = unicodeString.getClass().getMethod("getHangeul");
            Method getsID = unicodeString.getClass().getMethod("getsId");
//            Log.v(LOG_TAG, getHangeul.invoke(unicodeString, null).toString() + " invokemethod");
//            Log.v(LOG_TAG, getsID.invoke(unicodeString, null).toString() + " versus " + position);
            String[] tokens = getHangeul.invoke(unicodeString, null).toString().split(" ");
            for (String s : tokens) {
                word = word + String.valueOf((char) Integer.parseInt(s, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return word;
    }

}
