package com.example.android.bogipopmoviesstage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bogipopmoviesstage2.TheMovieContract.*;

/**
 * Created by Bogi on 2018. 04. 04..
 */

class TheMovieDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "movies.db";
    // The database version
    private static final int DATABASE_VERSION = 5;

    //This is the constructor:
    public TheMovieDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Favorites table to hold data
        final String SQL_CREATE_TABLE = "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Favorites.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                Favorites.COLUMN_TITLE + " TEXT NOT NULL, " +
                Favorites.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                Favorites.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                Favorites.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                Favorites.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                Favorites.COLUMN_VOTE_AVERAGE + " INTEGER, " +
                Favorites.COLUMN_ORI_TITLE + " TEXT" +
                "); ";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TheMovieContract.Favorites.TABLE_NAME);
        onCreate(db);
    }
}
