package com.awok.themoviedb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Umair on 8/26/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDB";

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE = "movies";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DATA = "data";

    // Database creation sql statement
    public static final String MoviesTable= "create table " + TABLE + " (" +
            COL_ID + " integer primary key, " +
            COL_TITLE + " text, " +
            COL_DATA + " blob"+ ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MoviesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
