package com.example.sahip.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahip.moviesapp.database.MovieListContract;
import com.example.sahip.moviesapp.database.MovielistDbHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final static String API_KEY="your api";
    public static final String IMAGE_URL_BASE_PATH = "http://image.tmdb.org/t/p/w185//";

    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_ORIGINAL_TITLE = "movie_original_title";
    public static final String MOVIE_IMAGE_URL = "movie_image_url";
    public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    private static final String TAG = DetailActivity.class.getSimpleName();

    ImageView moviePosterIv;
    Button reviewsBtn;
    Button trailersBtn;
    MaterialFavoriteButton favoriteBtn;
    TextView movieOriginalTitleTv;
    TextView movieOverviewTv;
    TextView movieReleaseDateTv;
    TextView movieVoteAverageTv;
    ListView reviewsLv;

    Integer movieId;
    String movieOriginalTitle;
    String movieImageUrl;
    Double movieVoteAverage;
    String movieOverview;
    String movieReleaseDate;

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize db helper class and get a writable database reference
        MovielistDbHelper movielistDbHelper = new MovielistDbHelper(this);
        mDb = movielistDbHelper.getWritableDatabase();

        moviePosterIv =  findViewById(R.id.movie_poster_image_thumbnail_iv);
        reviewsBtn =  findViewById(R.id.reviews_btn);
        trailersBtn = findViewById(R.id.trailers_btn);
        favoriteBtn =  findViewById(R.id.favorite_btn);
        movieOriginalTitleTv = findViewById(R.id.movie_original_title_tv);
        movieOverviewTv = findViewById(R.id.movie_overview_tv);
        movieVoteAverageTv = findViewById(R.id.movie_vote_average_tv);
        movieReleaseDateTv = findViewById(R.id.movie_release_date_tv) ;
        reviewsLv = findViewById(R.id.reviews_lv);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            movieId = extras.getInt(MOVIE_ID);
            movieOriginalTitle = extras.getString(MOVIE_ORIGINAL_TITLE);
            movieImageUrl = extras.getString(MOVIE_IMAGE_URL);
            movieVoteAverage = extras.getDouble(MOVIE_VOTE_AVERAGE);
            movieOverview = extras.getString(MOVIE_OVERVIEW);
            movieReleaseDate = extras.getString(MOVIE_RELEASE_DATE);

            final String image_url = IMAGE_URL_BASE_PATH + movieImageUrl;
            Picasso.with(this)
                    .load(image_url)
                    .into(moviePosterIv);

            movieOriginalTitleTv.setText(movieOriginalTitle);
            movieOverviewTv.setText(movieOverview);
            movieVoteAverageTv.setText(String.valueOf(movieVoteAverage));
            movieReleaseDateTv.setText(movieReleaseDate);

        }

        if(isFavoriteMovie()){
            // The movie is in the favorites collection
            favoriteBtn.setFavorite(true);
            favoriteBtn.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if(favorite){
                        addToFavorites();
                        Snackbar.make(buttonView, "Added to  favorites!",
                                Snackbar.LENGTH_SHORT).show();

                    }else {
                        deleteFromFavorites();
                        Snackbar.make(buttonView, "Removed from Favorites",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            // The movie is not in the favorites collection
            favoriteBtn.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if(favorite){
                        addToFavorites();
                        Snackbar.make(buttonView, "Added to  Favorites",
                                Snackbar.LENGTH_SHORT).show();

                    }else {
                        deleteFromFavorites();
                        Snackbar.make(buttonView, "Removed from Favorites",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }
            });


        }


        reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewsIntent = new Intent(getApplicationContext(), ReviewsActivity.class);
                reviewsIntent.putExtra(ReviewsActivity.MOVIE_ID, movieId );
                startActivity(reviewsIntent);
            }
        });

        trailersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trailersIntent = new Intent(getApplicationContext(), TrailersActivity.class);
                trailersIntent.putExtra(TrailersActivity.MOVIE_ID, movieId);
                startActivity(trailersIntent);
            }
        });


    }

    private boolean isFavoriteMovie() {

        Uri uri = MovieListContract.MovieListEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId.toString()).build();

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        Boolean result = (cursor.getCount() > 0);

        cursor.close();
        return result;
    }


    private Uri addToFavorites() {
        ContentValues cv = new ContentValues();

        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movieOriginalTitle);
        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_IMAGE_URL, movieImageUrl);
        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);
        cv.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);


        return getContentResolver().insert(MovieListContract.MovieListEntry.CONTENT_URI, cv);

    }

    private int deleteFromFavorites() {
        Uri uri = MovieListContract.MovieListEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId.toString()).build();

       return getContentResolver().delete(
               uri,
               null,
               null
       );
    }


}
