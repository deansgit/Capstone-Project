package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;

/**
 * Placeholder class used to generate Content Provider and Database via DBFlow
 */

@ContentProvider(authority = BodaDatabase.AUTHORITY,
        database = BodaDatabase.class,
        baseContentUri = BodaDatabase.BASE_CONTENT_URI)
@Database(name = BodaDatabase.NAME, version = BodaDatabase.VERSION)
public class BodaDatabase {
    public static final String NAME = "BodaDatabase";

    public static final int VERSION = 7;

    public static final String AUTHORITY = "com.runningoutofbreadth.boda.provider";

    public static final String BASE_CONTENT_URI = "content://";


    // Version 5 -- added Nations. Renamed some columns. Added pictures.

    // Version 6 -- added Idioms, Nation(flag) Pic references, and Vocabulary

    // Version 7 -- added one more column read/unread



}
