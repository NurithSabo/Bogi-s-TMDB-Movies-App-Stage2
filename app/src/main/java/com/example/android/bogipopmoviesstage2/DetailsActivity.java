package com.example.android.bogipopmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.example.android.bogipopmoviesstage2.MainActivity.AVERAGE_VOTE;
import static com.example.android.bogipopmoviesstage2.MainActivity.BACKDROP_PATH;
import static com.example.android.bogipopmoviesstage2.MainActivity.MOVIE_ID;
import static com.example.android.bogipopmoviesstage2.MainActivity.ORI_TITLE;
import static com.example.android.bogipopmoviesstage2.MainActivity.POSTER_PATH;
import static com.example.android.bogipopmoviesstage2.MainActivity.REL_DATE;
import static com.example.android.bogipopmoviesstage2.MainActivity.SYNOPSIS;
import static com.example.android.bogipopmoviesstage2.MainActivity.TITLE;

/**
 * Created by Bogi on 2018. 03. 15..
 */

   public class DetailsActivity extends AppCompatActivity {

     private RecyclerView mRecyclerViewReviews;
     private RecyclerView mRecyclerViewTrailers;
     private ReviewsAdapter mReviewAdapter;
     private TrailersAdapter mTrailerAdapter;
     private ArrayList<Review> mReviewList;
     private ArrayList<Trailer>mTrailerList;
     private String mMovieId;
     private TheMovie mMovie;
     private FloatingActionButton mFbLike;

    private Boolean isFavorite;
    private RequestQueue mRequestQueue;

    private void loadReviews(){
        mReviewList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        loadReviewsRequest(mMovieId);
    }

    private void loadTrailers(){
        mTrailerList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        loadTrailerRequest(mMovieId);
    }

    private void loadTrailerRequest(String movieIdentifier){
        String trailerUrl = Utils.buildUrlForTrailers(movieIdentifier).toString();
            //Log.e("Trailer URL", trailerUrl);

            final JsonObjectRequest arrayReq = new JsonObjectRequest(Request.Method.GET, trailerUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        try {
                            JSONArray trailerJsonArray = response.getJSONArray("results");

                            for (int i = 0; i < trailerJsonArray.length(); i++) {
                                JSONObject trailerObject = trailerJsonArray.getJSONObject(i);
                                Trailer trailer = new Trailer(trailerObject);
                                mTrailerList.add(trailer);
                            }
                            mTrailerAdapter = new TrailersAdapter(DetailsActivity.this, mTrailerList );
                            mRecyclerViewTrailers.setAdapter(mTrailerAdapter);

                        }
                        catch (JSONException je){
                            je.printStackTrace();
                        }}},

                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        } } );

            mRequestQueue.add(arrayReq);
        }

    private void loadReviewsRequest(String key){

        String reviewUrl = Utils.buildUrlForReviews(key).toString();
        //Log.e("Review URL",reviewUrl);

        final JsonObjectRequest arrayReq = new JsonObjectRequest(Request.Method.GET, reviewUrl,null,
                                     new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray moviesJsonArray = response.getJSONArray("results");

                    for (int i = 0; i < moviesJsonArray.length(); i++) {
                        JSONObject ReviewObject = moviesJsonArray.getJSONObject(i);
                        Review review = new Review(ReviewObject);
                        mReviewList.add(review);
                    }

                    mReviewAdapter = new ReviewsAdapter(DetailsActivity.this, mReviewList);
                    mRecyclerViewReviews.setAdapter(mReviewAdapter);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    } } );

        mRequestQueue.add(arrayReq);
}

    private void changeFavoriteStatus() {

        if (isFavorite) {
            deleteMovieFromDatabase();}
        else {
            addMovieToDatabase();}
    }

    private  void deleteMovieFromDatabase(){
        Integer isDeleted = getApplicationContext().getContentResolver().delete(
                        TheMovieContract.Favorites.CONTENT_URI,
                        TheMovieContract.Favorites.COLUMN_MOVIE_ID + " = ?",
                        new String[]{mMovie.getId()});

        if (isDeleted>0){
            Toast.makeText(getApplicationContext(), mMovie.getTitle() +
                    " is deleted from favorites", Toast.LENGTH_LONG).show();
            isFavorite = false;
            setFavoriteHeart();
    Log.e("delete movies from data", isDeleted+"");

        } else {
            Toast.makeText(getApplicationContext(),
                    " Ooops, something went wrong", Toast.LENGTH_LONG).show();
        }

    }

 // hint were from this: https://anujarosha.wordpress.com/2011/12/19/how-to-retrieve-data-from-a-sqlite-database-in-android/
    private void addMovieToDatabase(){

        Utils.AddMovieToDatabase(getApplicationContext(), mMovie);
        Toast.makeText(getApplicationContext(), mMovie.getTitle() +
                " is added to favorites", Toast.LENGTH_SHORT).show();

        isFavorite = !isFavorite;
        setFavoriteHeart();
    }

    private void setFavoriteHeart(){
        if(isFavorite) {
            mFbLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sziv_piros));
        }
        else{
            mFbLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sziv_lyukas));
        }
    }

    private void setMovieData(final TheMovie mmMovie){
        isFavorite = Utils.isMovieOnDatabase(getApplicationContext(),Integer.parseInt(mmMovie.getId()));
        setFavoriteHeart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mRecyclerViewReviews = findViewById(R.id.recy_review);
        mRecyclerViewTrailers = findViewById(R.id.recy_video);

        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewTrailers.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecyclerViewReviews.setLayoutManager(manager);

        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewTrailers.setLayoutManager(manager1);

        Intent intent = getIntent();
        String posterImageUrl = intent.getStringExtra(POSTER_PATH);
        String title = intent.getStringExtra(TITLE);
        String[] releaseDate = intent.getStringExtra(REL_DATE).split("-");
        String avgVote = intent.getStringExtra(String.valueOf(AVERAGE_VOTE));
        String oriTitle = intent.getStringExtra(ORI_TITLE);
        String overview = intent.getStringExtra(SYNOPSIS);
        String backdrop = intent.getStringExtra(BACKDROP_PATH);
        final String currentId = intent.getStringExtra(String.valueOf(MOVIE_ID));

        mMovieId = currentId;

        mMovie = new TheMovie(posterImageUrl,title,releaseDate[0],
                Double.parseDouble(avgVote),oriTitle,overview,
                backdrop, mMovieId);

        TextView textViewTitle = findViewById(R.id.title_of_movie);
        TextView textViewDate = findViewById(R.id.release_date);
        TextView textViewVote = findViewById(R.id.textview_vote_average);
        TextView textViewOriTitle = findViewById(R.id.textview_original_title);
        TextView textViewSyno = findViewById(R.id.textview_overview);
        ImageView imageBackdrop = findViewById(R.id.keppppp);
        mFbLike = (FloatingActionButton) findViewById(R.id.fbLike);

        setMovieData(mMovie);
        mFbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeFavoriteStatus();
            }
        });

        Picasso.with(this)
                .load(backdrop)
                .into(imageBackdrop);

        textViewTitle.setText(title);
        textViewDate.setText(releaseDate[0]);
        textViewVote.setText(avgVote+" "+getResources().getString(R.string.average_rating));
        textViewOriTitle.setText(oriTitle);
        textViewSyno.setText(overview);
        loadReviews();
        loadTrailers();
    }
}
