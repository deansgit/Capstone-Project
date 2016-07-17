package com.runningoutofbreadth.boda.db;

/**
 * Created by SandD on 7/3/2016.
 */
public class WordFactory {

    public static Word build(Class category) throws IllegalAccessException, InstantiationException {
        if (category == null){
            return null;
        }
//
//        if (category.getSimpleName().equalsIgnoreCase("SYLLABLE")){
//            return new Syllable();
//        }else if (category.getSimpleName().equalsIgnoreCase("ANIMAL")){
//            return new Animal();
//        }else if
        return (Word) category.newInstance();
    }

}
