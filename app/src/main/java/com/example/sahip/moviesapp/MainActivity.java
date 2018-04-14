package com.example.sahip.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sahip.moviesapp.adapters.MovieAdapter;
import com.example.sahip.moviesapp.database.MovieListContract;
import com.example.sahip.moviesapp.database.MovielistDbHelper;
import com.example.sahip.moviesapp.models.Movie;
import com.example.sahip.moviesapp.models.MovieResponse;
import com.example.sahip.moviesapp.rest.MovieService;
import com.example.sahip.moviesapp.rest.RestClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY="YOUR API KEY HERE";

    List<Movie> movieList = new ArrayList<>();
    List<Movie> favoriteList = new ArrayList<>();
    RecyclerView moviesRV;
    MovieService movieService;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize db helper class and get a readable database reference
        MovielistDbHelper movielistDbHelper = new MovielistDbHelper(this);
        mDb = movielistDbHelper.getReadableDatabase();

        moviesRV = findViewById(R.id.movies_rv);
        moviesRV.setLayoutManager(new GridLayoutManager(this, 2));


        String sortOrder = setUpSharedPreferences();
        if(sortOrder.equals(getString(R.string.pref_sort_order_favorites_key))){
            sortByFavoriteMovies();
        }
        else{
            getMoviesFromAPI(sortOrder);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortOrder = setUpSharedPreferences();
        if(sortOrder.equals(getString(R.string.pref_sort_order_favorites_key))){
            sortByFavoriteMovies();
        }
        else{
            getMoviesFromAPI(sortOrder);
        }


    }

    private void sortByFavoriteMovies(){
        Cursor cursor = getAllFavoriteMovies();
        createListFromCursor(cursor);

        moviesRV.setAdapter(new MovieAdapter(getApplicationContext(), favoriteList));
    }

    private void createListFromCursor(Cursor cursor) {
        if(!favoriteList.isEmpty()) {
            favoriteList.clear();
        }

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Integer movieId = cursor
                    .getInt(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_ID));
            String movieOriginalTitle = cursor
                    .getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
            String movieImageUrl = cursor
                    .getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_IMAGE_URL));
            Double movieVoteAverage = cursor
                    .getDouble(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE));
            String movieOverview = cursor
                    .getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW));
            String movieReleaseDate = cursor
                    .getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE));

            Movie movie = new Movie();

            movie.setId(movieId);
            movie.setOriginalTitle(movieOriginalTitle);
            movie.setPosterPath(movieImageUrl);
            movie.setOverview(movieOverview);
            movie.setVoteAverage(movieVoteAverage);
            movie.setReleaseDate(movieReleaseDate);

            favoriteList.add(movie);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private String setUpSharedPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return sharedPreferences.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_top_rated_key));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getMoviesFromAPI(String sortOrder) {

       movieService =  RestClient.getClient().create(MovieService.class);
        Call<MovieResponse> call;

       if(sortOrder.equals(getString(R.string.pref_sort_order_top_rated_key))){
          call = movieService.getTopRatedMovies(API_KEY);

       }else {
          call = movieService.getPopularMovies(API_KEY);

       }


       call.enqueue(new Callback<MovieResponse>() {
           @Override
           public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
               movieList = response.body().getResults();
               moviesRV.setAdapter(new MovieAdapter(getApplicationContext(), movieList));
               Log.i(TAG, "Number of movies received: " + movieList.size());

           }

           @Override
           public void onFailure(Call<MovieResponse> call, Throwable t) {
               Toast.makeText(getApplicationContext(), "Unable to fetch movie data. No network connection",
                       Toast.LENGTH_LONG).show();

           }
       });

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_order_key))) {

            String sortOrder =
                    sharedPreferences.getString(key, getString(R.string.pref_sort_order_top_rated_label));

            if(sortOrder.equals(getString(R.string.pref_sort_order_favorites_key))){
                sortByFavoriteMovies();
            }
            else{
                getMoviesFromAPI(sortOrder);
            }
        }

    }


    private Cursor getAllFavoriteMovies() {
        return getContentResolver().query(
                MovieListContract.MovieListEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

}
