package com.runningoutofbreadth.boda;

/**
 * Created by SandD on 7/3/2016.
 */
public class WordFactory {

    public static Word getWord(Class wordType){
        if (wordType == null){
            return null;
        }
        if (wordType.getSimpleName().equalsIgnoreCase("SYLLABLE")){
            return new Syllable();
        }else if (wordType.getSimpleName().equalsIgnoreCase("ANIMAL")){
            return new Animal();
        }
        return null;
    }

}
