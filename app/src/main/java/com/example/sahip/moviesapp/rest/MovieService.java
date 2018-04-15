package com.example.sahip.moviesapp.rest;

import com.example.sahip.moviesapp.BuildConfig;
import com.example.sahip.moviesapp.models.MovieResponse;
import com.example.sahip.moviesapp.models.ReviewResponse;
import com.example.sahip.moviesapp.models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sahip on 28.02.2018.
 */

public interface MovieService {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(@Path("movie_id" ) Integer id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") Integer id, @Query("api_key") String apiKey);
}
