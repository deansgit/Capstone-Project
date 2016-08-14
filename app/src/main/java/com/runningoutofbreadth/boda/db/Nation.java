package com.runningoutofbreadth.boda.db;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

/**
 * Nation model of db object
 */
@TableEndpoint(name = Nation.NAME, contentProvider = BodaDatabase.class)
@Table(database = BodaDatabase.class, name = Nation.NAME)
public class Nation extends BaseProviderModel<Nation> implements Word {

    public static final String NAME = "Nation";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = ContentUtils.buildUriWithAuthority(BodaDatabase.AUTHORITY);


    // identifier int within the db
    @PrimaryKey
    int sId;

    @Column
    String nation_name;

    // unicode hex
    @Column
    String hangeul;

    // Revised Romanization of Korean - pronunciation
    @Column
    String romanization;

    @Column
    String imageId;

    @Column
    boolean read;

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isRead() {
        return read;
    }

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
        return nation_name;
    }

    @Override
    public void setTranslation(String nation_name) {
        this.nation_name = nation_name;
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

    @Override
    public Uri getDeleteUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return CONTENT_URI;
    }
}