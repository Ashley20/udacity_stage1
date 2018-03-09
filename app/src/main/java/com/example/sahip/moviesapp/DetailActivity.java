package com.example.sahip.moviesapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {


    public static final String MOVIE_ORIGINAL_TITLE = "movie_original_title";
    public static final String MOVIE_IMAGE_URL = "movie_image_url";
    public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";

    ImageView moviePosterIv;
    TextView movieOriginalTitleTv;
    TextView movieOverviewTv;
    TextView movieReleaseDateTv;
    TextView movieVoteAverageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        moviePosterIv = (ImageView) findViewById(R.id.movie_poster_image_thumbnail_iv);
        movieOriginalTitleTv = (TextView) findViewById(R.id.movie_original_title_tv);
        movieOverviewTv = (TextView) findViewById(R.id.movie_overview_tv);
        movieVoteAverageTv = (TextView) findViewById(R.id.movie_vote_average_tv);
        movieReleaseDateTv = (TextView) findViewById(R.id.movie_release_date_tv) ;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            String movieOriginalTitle = extras.getString(MOVIE_ORIGINAL_TITLE);
            String movieImageUrl = extras.getString(MOVIE_IMAGE_URL);
            Double movieVoteAverage = extras.getDouble(MOVIE_VOTE_AVERAGE);
            String movieOverview = extras.getString(MOVIE_OVERVIEW);
            String movieReleaseDate = extras.getString(MOVIE_RELEASE_DATE);

            Picasso.with(this)
                    .load(movieImageUrl)
                    .into(moviePosterIv);

            movieOriginalTitleTv.setText(movieOriginalTitle);
            movieOverviewTv.setText(movieOverview);
            movieVoteAverageTv.setText(String.valueOf(movieVoteAverage));
            movieReleaseDateTv.setText(movieReleaseDate);



        }



    }


}
