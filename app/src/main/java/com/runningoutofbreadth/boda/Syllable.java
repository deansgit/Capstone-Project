package com.runningoutofbreadth.boda;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Syllable model of db object
 */
@Table (database = BodaDatabase.class)
public class Syllable extends BaseModel{

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

//    public Syllable(int sId, String unicode_name, String syllable, String romanization){
//        this.sId = sId;
//        this.unicode_name = unicode_name;
//        this.syllable = syllable;
//        this.romanization = romanization;
//    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public void setUnicode_name(String unicode_name) {
        this.unicode_name = unicode_name;
    }

    public void setSyllable(String syllable) {
        this.syllable = syllable;
    }

    public void setRomanization(String romanization) {
        this.romanization = romanization;
    }

    public int getsId() {
        return sId;
    }

    public String getUnicode_name() {
        return unicode_name;
    }

    public String getSyllable() {
        return syllable;
    }

    public String getRomanization() {
        return romanization;
    }

}
