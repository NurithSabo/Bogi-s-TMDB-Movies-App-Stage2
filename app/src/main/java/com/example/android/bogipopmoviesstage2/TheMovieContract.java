package com.example.android.bogipopmoviesstage2;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bogi on 2018. 04. 04..
 */

class TheMovieContract {
    static final String AUTHORITY = "com.example.android.bogipopmoviesstage2";
    static final String PATH_MOVIES = "movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class Favorites implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                        .buildUpon()
                        .appendPath(PATH_MOVIES)
                        .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;
        public static final String TABLE_NAME = "movies_table";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        //public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_ORI_TITLE = "original_title";
    }
}
