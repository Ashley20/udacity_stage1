package com.example.sahip.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private final static String API_KEY="YOUR API KEY HERE";
    public final static String MOVIE_ID="movie_id";

    public ListView reviewsLv;
    MovieService movieService;
    ArrayList<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsLv =  findViewById(R.id.reviews_lv);

        movieService = RestClient.getClient().create(MovieService.class);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            Integer  movieId = extras.getInt(MOVIE_ID);
            getMovieReviews(movieId);
        }
    }

    private void getMovieReviews(Integer movieId) {
        Call<ReviewResponse> call = movieService.getReviews(movieId, API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                reviewList = new ArrayList<>();
                reviewList.addAll(response.body().getResults());
                reviewsLv.setAdapter(new ReviewsAdapter(getApplicationContext(), reviewList));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
