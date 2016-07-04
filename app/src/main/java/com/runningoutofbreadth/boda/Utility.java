package com.runningoutofbreadth.boda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SandD on 7/1/2016.
 */
public class Utility {

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

}
