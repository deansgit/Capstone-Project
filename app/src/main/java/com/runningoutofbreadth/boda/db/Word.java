package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by SandD on 7/1/2016.
 */
public interface Word {

    void makeObject();

    void setsId(int sId);

    void setTranslation(String name);

    void setHangeul(String hangeul);

    void setRomanization(String romanization);

    void setImageId(String imageId);

    void save(DatabaseWrapper wrapper);

    int getsId();

    String getTranslation();

    String getHangeul();

    String getRomanization();

    String getImageId();


}
