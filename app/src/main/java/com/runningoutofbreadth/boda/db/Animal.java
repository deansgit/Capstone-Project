package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Animal model of db object
 */
@Table(database = BodaDatabase.class)
public class Animal extends BaseModel implements Word {

    // identifier int within the db
    @PrimaryKey
    int sId;

    @Column
    String animal_name;

    // unicode hex
    @Column
    String syllable;

    // Revised Romanization of Korean - pronunciation
    @Column
    String romanization;

    @Column
    String imageId;

    @Override
    public void makeObject() {
    }

    @Override
    public void setsId(int sId) {
        this.sId = sId;
    }

    @Override
    public void setName(String animal_name) {
        this.animal_name = animal_name;
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
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public void save(DatabaseWrapper databaseWrapper){
        getModelAdapter().save(this, databaseWrapper);
    }

    @Override
    public int getsId() {
        return sId;
    }

    @Override
    public String getName() {
        return animal_name;
    }

    @Override
    public String getHangeul() {
        return syllable;
    }

    @Override
    public String getRomanization() {
        return romanization;
    }

    @Override
    public String getImageId() {
        return imageId;
    }
}
