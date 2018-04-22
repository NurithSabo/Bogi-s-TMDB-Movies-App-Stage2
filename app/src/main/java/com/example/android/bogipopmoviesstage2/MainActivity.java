package com.example.android.bogipopmoviesstage2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TheMovieAdapter.OnItemClickListener{

    /* https://www.youtube.com/watch?v=bPDW9SdsGMo&list=PLrnPJCHvNZuBCiCxN8JPFI57Zhr5SusRL*/

    public static final String POSTER_PATH = "imageUrl";
    public static final String TITLE = "title";
    public static final String REL_DATE = "releaseDate";
    public static final double AVERAGE_VOTE = 3.3;
    public static final String ORI_TITLE = "originalTitle";
    public static final String SYNOPSIS = "synopsis";
    public static final String BACKDROP_PATH = "backdropPath";
    public static final String MOVIE_ID = "100";

    private static String optionOfSort = "popular";
    public static String getOptionOfSort() { return optionOfSort; }

    private RecyclerView mRecyclerView;
    private TheMovieAdapter mTheMovieAdapter;
    private ArrayList<TheMovie> mTheMovieList;
    private RequestQueue mRequestQueue;
    private SQLiteDatabase database;
    private ArrayList<TheMovie> favorites;

    private void loadTmdbData(){
        Log.e("optSort",optionOfSort);
        mTheMovieList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON(optionOfSort);
    }

    /*https://developer.android.com/training/volley/request.html*/
    private void parseJSON(String sort) {
        String url = Utils.buildUrl(sort).toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject eredmeny = jsonArray.getJSONObject(i);

                                String imageUrl = eredmeny.getString("poster_path");
                                String title = eredmeny.getString("title");
                                String releaseDate = eredmeny.getString("release_date");
                                double averageVote = Double.parseDouble(eredmeny.getString("vote_average"));
                                String originalTitle = eredmeny.getString("original_title");
                                String overView = eredmeny.getString("overview");
                                String backDrop = eredmeny.getString("backdrop_path");
                                String movieId = eredmeny.getString("id");

                                mTheMovieList.add(new TheMovie(imageUrl,title,releaseDate,
                                        averageVote,originalTitle, overView,backDrop,movieId));
                            }

                            mTheMovieAdapter = new TheMovieAdapter(MainActivity.this, mTheMovieList);
                            mRecyclerView.setAdapter(mTheMovieAdapter);
                            mTheMovieAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("VolleyHiba",error.getMessage());
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView =  findViewById(R.id.recy_pecy);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(manager);


        if(optionOfSort.equals(getString(R.string.menu_popular))){
            loadTmdbData();}

        else if (optionOfSort.equals(getResources().getString(R.string.top_rated))){
            loadTmdbData();
        }
        else if (optionOfSort.equals(getString(R.string.menu_favorite))){
            showFavorites();
        }
        else{Log.e("Oh, no!", "Something went wrong at Main onCreate :-(");}
        }

    @Override
    public void onItemClick(int position) {

        if(optionOfSort.equals(getResources().getString(R.string.menu_favorite))){
            Intent inte = new Intent(this, DetailsActivity.class);
            TheMovie clicked = favorites.get(position);
                    inte.putExtra(String.valueOf(MOVIE_ID),clicked.getId());
                    inte.putExtra(TITLE, clicked.getTitle());
                    inte.putExtra(SYNOPSIS,clicked.getSynopsis());
                    inte.putExtra(REL_DATE, clicked.getReleaseDate());
                    inte.putExtra(POSTER_PATH, clicked.getPosterPath2());
                    inte.putExtra(BACKDROP_PATH,clicked.getBackdropPath2());
                    inte.putExtra(String.valueOf(AVERAGE_VOTE), String.valueOf(clicked.getVoteAverage()));
                    inte.putExtra(ORI_TITLE,clicked.getOriginalTitle());
                    startActivity(inte);
        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            TheMovie clickedItem = mTheMovieList.get(position);

                    intent.putExtra(String.valueOf(MOVIE_ID),clickedItem.getId());
                    intent.putExtra(TITLE, clickedItem.getTitle());
                    intent.putExtra(SYNOPSIS,clickedItem.getSynopsis());
                    intent.putExtra(REL_DATE, clickedItem.getReleaseDate());
                    intent.putExtra(POSTER_PATH, clickedItem.getPosterPath());
                    intent.putExtra(BACKDROP_PATH,clickedItem.getBackdropPath());
                    intent.putExtra(String.valueOf(AVERAGE_VOTE), String.valueOf(clickedItem.getVoteAverage()));
                    intent.putExtra(ORI_TITLE,clickedItem.getOriginalTitle());
                    startActivity(intent);
            }
    }

/*Settings of the menu:
    https://developer.android.com/guide/topics/ui/menus.html#checkable */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem popularityMenuItem = menu.findItem(R.id.sort_popularity);
        MenuItem ratingMenuItem = menu.findItem(R.id.sort_rating);
        MenuItem favoriteMenuItem = menu.findItem(R.id.sort_favorites);

        if (optionOfSort != null && optionOfSort.equals(getResources().getString(R.string.menu_popular))) {
            if (popularityMenuItem != null && !popularityMenuItem.isChecked()) {
                popularityMenuItem.setChecked(true);
            }
        }
        else if (optionOfSort != null && optionOfSort.equals(getResources().getString(R.string.top_rated))) {
            if (ratingMenuItem != null && !ratingMenuItem.isChecked()) {
                ratingMenuItem.setChecked(true);
            }
        }
        else if(optionOfSort != null && optionOfSort.equals(getResources().getString(R.string.menu_favorite))){
            if(favoriteMenuItem != null && !favoriteMenuItem.isChecked()){
                favoriteMenuItem.setChecked(true);
            }
        }
        else {
            optionOfSort = getResources().getString(R.string.menu_popular);
            popularityMenuItem.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_popularity:
                optionOfSort = getString(R.string.menu_popular);
                Check(item);
                loadTmdbData();
                return true;
            case R.id.sort_rating:
                optionOfSort = getString(R.string.top_rated);
                Check(item);
                loadTmdbData();
                return true;
            case R.id.sort_favorites:
                optionOfSort = getString(R.string.menu_favorite);
                Check(item);
                showFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Check(MenuItem item){
        if(item.isChecked()){
            item.setChecked(false);}
        else item.setChecked(true);
    }

        private ArrayList<TheMovie> listOfFavorites(){

        favorites = new ArrayList<>();
        TheMovieDbHelper helper = new TheMovieDbHelper(getApplicationContext());
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(TheMovieContract.Favorites.TABLE_NAME,null,
                null,null,null,null,null);

        while (cursor.moveToNext()){
            favorites.add(new TheMovie(cursor));
        }
        cursor.close();
        return favorites;
    }

        private void showFavorites(){
        ArrayList<TheMovie>favList = listOfFavorites();
        mTheMovieAdapter = new TheMovieAdapter(MainActivity.this, favList);
        mRecyclerView.setAdapter(mTheMovieAdapter);
        mTheMovieAdapter.setOnItemClickListener(MainActivity.this);
    }

}