package com.example.sahip.moviesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovielistDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = MovielistDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movielist.db";
    private static final int DATABASE_VERSION = 2;

    public MovielistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " +
                MovieListContract.MovieListEntry.TABLE_NAME + "(" +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_IMAGE_URL + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE + " DOUBLE NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIELIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading the database from version: " + oldVersion + " to: " + newVersion);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieListContract.MovieListEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }
}
