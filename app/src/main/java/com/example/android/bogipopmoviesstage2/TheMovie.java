package com.example.android.bogipopmoviesstage2;

import android.database.Cursor;

/**
 * Created by Bogi on 2018. 03. 15..
 */

class TheMovie {
    private  String posterPath;
    private  String title;
    private  String releaseDate;
    private  double voteAverage;
    private  String synopsis;
    private  String originalTitle;
    private  String backdropPath;
    private  String id;


    public TheMovie(String posterPath, String title, String releaseDate,
                    double voteAverage, String originalTitle, String synopsis, String backDropPath, String id) {

        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.originalTitle = originalTitle;
        this.synopsis = synopsis;
        this.backdropPath = backDropPath;
        this.id = id;
    }

    public TheMovie(Cursor cursor) {
        try
        {
            this.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_MOVIE_ID)));

            this.title = cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_TITLE));
            this.synopsis= cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_OVERVIEW));
            this.releaseDate = cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_RELEASE_DATE));
            this.posterPath = cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_POSTER_PATH));
            this.backdropPath = cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_BACKDROP_PATH));
            this.voteAverage = cursor.getInt(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_VOTE_AVERAGE));
            this.originalTitle = cursor.getString(cursor.getColumnIndex(TheMovieContract.Favorites.COLUMN_ORI_TITLE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getId() { return id; }

    public String getBackdropPath() { return "http://image.tmdb.org/t/p/w342/"+backdropPath; }

    public String getBackdropPath2(){return backdropPath;}

    public String getOriginalTitle() { return originalTitle; }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w342/"+posterPath;
    }

    public String getPosterPath2(){return posterPath;}

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTitle() {
        return title;
    }

}
