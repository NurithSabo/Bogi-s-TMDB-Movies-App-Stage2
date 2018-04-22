package com.example.android.bogipopmoviesstage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bogi on 2018. 03. 15..
 */

class Utils{
    private final static String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String API_QUERY = "api_key";
    private  static final String API_KEY =  "84ffdd5222490bab19a1d49dfdc96a44";


    /*Build an URL*/
    public static URL buildUrl(String tmdbSearchQuery) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(tmdbSearchQuery)
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException mUEx) {
            mUEx.printStackTrace();
        }
        return url;
    } //buildUrl vége


    public static URL buildUrlForReviews(String movieId) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForTrailers (String movieId){
        Uri buildUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                       .appendPath(movieId)
                       .appendPath("videos")
                       .appendQueryParameter(API_QUERY, API_KEY)
                       .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());

        }
        catch (MalformedURLException mfe){
            mfe.printStackTrace();
        }
        return url;
    }


    public static Boolean isMovieOnDatabase(Context context, int id) {
        TheMovieDbHelper dbHelper = new TheMovieDbHelper(context);
        SQLiteDatabase mDb = dbHelper.getReadableDatabase();
        Cursor cursor = mDb.query(
                TheMovieContract.Favorites.TABLE_NAME,
                null,
                TheMovieContract.Favorites.COLUMN_MOVIE_ID + " = ?",
                new String[] { Integer.toString(id) },
                null, null, null
        );
        int numRows = cursor.getCount();
        cursor.close();
        mDb.close();
        return numRows > 0 ;
    }

    public static void AddMovieToDatabase(Context context, TheMovie movie){
        ContentValues values = new ContentValues();

        values.put(TheMovieContract.Favorites.COLUMN_MOVIE_ID,movie.getId());
        values.put(TheMovieContract.Favorites.COLUMN_TITLE, movie.getTitle());
        values.put(TheMovieContract.Favorites.COLUMN_OVERVIEW, movie.getSynopsis());
        values.put(TheMovieContract.Favorites.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(TheMovieContract.Favorites.COLUMN_POSTER_PATH,movie.getPosterPath2());
        values.put(TheMovieContract.Favorites.COLUMN_BACKDROP_PATH,movie.getBackdropPath2());
        values.put(TheMovieContract.Favorites.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(TheMovieContract.Favorites.COLUMN_ORI_TITLE,movie.getOriginalTitle());

        Uri rUri = context.getContentResolver().insert(TheMovieContract.Favorites.CONTENT_URI, values);
        Log.i("MovieDb Updated::->", "rUri: " + rUri);

    }

}