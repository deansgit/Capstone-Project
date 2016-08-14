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
 * Syllable model of db object
 */
@TableEndpoint(name = Syllable.NAME, contentProvider = BodaDatabase.class)
@Table(database = BodaDatabase.class, name = Syllable.NAME)
public class Syllable extends BaseProviderModel<Syllable> implements Word {

    public static final String NAME = "Syllable";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = ContentUtils.buildUriWithAuthority(BodaDatabase.AUTHORITY);

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

    @Column
    boolean read;

    @Override
    public void makeObject() {
    }

    @Override
    public int getsId() {
        return sId;
    }

    @Override
    public String getTranslation() {
        return unicode_name;
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
    public void setsId(int sId) {
        this.sId = sId;
    }

    @Override
    public void setTranslation(String unicode_name) {
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
    public void save(DatabaseWrapper databaseWrapper) {
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

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
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
