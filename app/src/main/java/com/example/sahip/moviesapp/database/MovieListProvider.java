package com.example.sahip.moviesapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.sahip.moviesapp.adapters.MovieAdapter;

public class MovieListProvider extends ContentProvider{
    private MovielistDbHelper dbHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();


    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieListContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieListContract.MovieListEntry.TABLE_NAME, MOVIE);
        uriMatcher.addURI(authority, MovieListContract.MovieListEntry.TABLE_NAME + "/#", MOVIE_WITH_ID);

        return uriMatcher;

    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovielistDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor mCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                mCursor = dbHelper.getReadableDatabase().query(
                        MovieListContract.MovieListEntry.TABLE_NAME,
                        projections,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return mCursor;

            case MOVIE_WITH_ID:
                mCursor = dbHelper.getReadableDatabase().query(
                        MovieListContract.MovieListEntry.TABLE_NAME,
                        projections,
                        MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return mCursor;
            default:
                Toast.makeText(getContext(), "Invalid content uri", Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                return MovieListContract.MovieListEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MovieListContract.MovieListEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                long _id = db.insert(MovieListContract.MovieListEntry.TABLE_NAME, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MovieListContract.MovieListEntry.buildMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numDeleted;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                numDeleted = db.delete(MovieListContract.MovieListEntry.TABLE_NAME,
                        MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (numDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}