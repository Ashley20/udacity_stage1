package com.example.sahip.moviesapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sahip.moviesapp.adapters.ReviewsAdapter;
import com.example.sahip.moviesapp.models.Review;
import com.example.sahip.moviesapp.models.ReviewResponse;
import com.example.sahip.moviesapp.rest.MovieService;
import com.example.sahip.moviesapp.rest.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {
    public static final String TAG = ReviewsActivity.class.getSimpleName();
    public final static String MOVIE_ID="movie_id";
    public static final String LISTVIEW_STATE = "listview_state";

    public ListView reviewsLv;
    MovieService movieService;
    ArrayList<Review> reviewList;
    Parcelable state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        if(savedInstanceState != null){
            Log.d(TAG, "trying to restore listview state..");
            state = savedInstanceState.getParcelable(LISTVIEW_STATE);
        }

        reviewsLv =  findViewById(R.id.reviews_lv);
        movieService = RestClient.getClient().create(MovieService.class);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            Integer  movieId = extras.getInt(MOVIE_ID);
            getMovieReviews(movieId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LISTVIEW_STATE, reviewsLv.onSaveInstanceState());
        Log.d(TAG, "saving listview state @ onSaveInstance");
    }

    private void getMovieReviews(Integer movieId) {
        Call<ReviewResponse> call = movieService.getReviews(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                reviewList = new ArrayList<>();
                reviewList.addAll(response.body().getResults());
                reviewsLv.setAdapter(new ReviewsAdapter(getApplicationContext(), reviewList));
                if(state != null){
                    reviewsLv.onRestoreInstanceState(state);
                }

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
