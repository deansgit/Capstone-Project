package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Nation model of db object
 */
@Table(database = BodaDatabase.class)
public class Idiom extends BaseModel implements Word {

    // identifier int within the db
    @PrimaryKey
    int sId;

    @Column
    String idiomatic_expression;

    // unicode hex
    @Column
    String hangeul;

    // Revised Romanization of Korean - pronunciation
    @Column
    String romanization;

    @Column
    String imageId;

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public int getsId() {
        return sId;
    }

    @Override
    public void setsId(int sId) {
        this.sId = sId;
    }

    @Override
    public String getTranslation() {
        return idiomatic_expression;
    }

    @Override
    public void setTranslation(String nation_name) {
        this.idiomatic_expression = nation_name;
    }

    @Override
    public void makeObject() {

    }

    @Override
    public String getHangeul() {
        return hangeul;
    }

    @Override
    public void setHangeul(String hangeul) {
        this.hangeul = hangeul;
    }

    @Override
    public String getRomanization() {
        return romanization;
    }

    @Override
    public void setRomanization(String romanization) {
        this.romanization = romanization;
    }


    @Override
    public void save(DatabaseWrapper databaseWrapper) {
        getModelAdapter().save(this, databaseWrapper);
    }
}