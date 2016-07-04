package com.runningoutofbreadth.boda;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Syllable model of db object
 */
@Table(database = BodaDatabase.class)
public class Syllable extends BaseModel implements Word {

    // identifier int within the db
    @PrimaryKey
    int sId;

    @Column
    String unicode_name;

    // unicode hex
    @Column
    String syllable;

    // Revised Romanization of Korean - pronunciation
    @Column
    String romanization;

    @Override
    public void makeObject() {
    }

    @Override
    public int getsId() {
        return sId;
    }

    @Override
    public String getName() {
        return unicode_name;
    }

    @Override
    public String getHangeul() {
        return syllable;
    }

    public String getSyllable() {
        return syllable;
    }

    public String getRomanization() {
        return romanization;
    }

    @Override
    public void setsId(int sId) {
        this.sId = sId;
    }

    @Override
    public void setName(String unicode_name) {
        this.unicode_name = unicode_name;
    }

    @Override
    public void setHangeul(String syllable) {
        this.syllable = syllable;
    }

    @Override
    public void setRomanization(String romanization) {
        this.romanization = romanization;
    }

    @Override
    public void save(DatabaseWrapper databaseWrapper){
        getModelAdapter().save(this, databaseWrapper);
    }

    // does nothing
    @Override
    public void setImageId(String imageId) {
    }

    // does nothing
    @Override
    public String getImageId() {
        return null;
    }



}
