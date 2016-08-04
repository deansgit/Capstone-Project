package com.runningoutofbreadth.boda.db;

/**
 * Created by SandD on 7/3/2016.
 */
public class WordFactory {

    public static Word build(Class category) throws IllegalAccessException, InstantiationException {
        if (category == null){
            return null;
        }
        return (Word) category.newInstance();
    }

//    public static Word build(Class category, int position) throws IllegalAccessException, InstantiationException {
//        if (category == null){
//            return null;
//        }
//        NameAlias alias = NameAlias.builder("sId").build();
//        Condition randomIdCondition = Condition.column(alias).eq(position);
//        Model wordItem = SQLite.select()
//                    .from(category)
//                    .where(randomIdCondition)
//                    .querySingle();
//        Word newWord = (Word) category.newInstance();
//        newWord.setsId(position);
//        newWord.setTranslation(wordItem.);
//        return newWord;
//    }

}
