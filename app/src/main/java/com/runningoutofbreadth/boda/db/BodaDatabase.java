package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by SandD on 6/20/2016.
 */
@Database(name = BodaDatabase.NAME, version = BodaDatabase.VERSION)
public class BodaDatabase{
    public static final String NAME = "BodaDatabase";

    public static final int VERSION = 7;

    // Version 5 -- added Nations. Renamed some columns. Added pictures.

    // Version 6 -- added Idioms, Nation(flag) Pic references, and Vocabulary

    // Version 7 -- added one more column read/unread
}
