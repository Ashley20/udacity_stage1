package com.example.sahip.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sahip.moviesapp.adapters.TrailersAdapter;
import com.example.sahip.moviesapp.models.Video;
import com.example.sahip.moviesapp.models.VideoResponse;
import com.example.sahip.moviesapp.rest.MovieService;
import com.example.sahip.moviesapp.rest.RestClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersActivity extends AppCompatActivity {
    public static final String TAG = TrailersActivity.class.getSimpleName();
    public static final String LISTVIEW_STATE = "listview_state";
    public static final String MOVIE_ID = "movie_id";

    ListView trailersLv;
    ArrayList<Video> videoList;
    MovieService movieService;
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        if(savedInstanceState != null){
            Log.d(TAG, "trying to restore listview state..");
            state = savedInstanceState.getParcelable(LISTVIEW_STATE);
        }


        trailersLv = findViewById(R.id.trailers_lv);
        movieService = RestClient.getClient().create(MovieService.class);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            Integer movieId = extras.getInt(MOVIE_ID);
            getMovieVideos(movieId);
        }

        trailersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = trailersLv.getItemAtPosition(position);
                Video video = (Video)o;

                String url = "https://www.youtube.com/watch?v=" + video.getKey();

                playMedia(url);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LISTVIEW_STATE, trailersLv.onSaveInstanceState());
        Log.d(TAG, "saving listview state @ onSaveInstance");
    }

    private void getMovieVideos(Integer movieId) {
        Call<VideoResponse> call = movieService.getVideos(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {

                videoList = new ArrayList<>();
                videoList.addAll(response.body().getResults());
                trailersLv.setAdapter(new TrailersAdapter(getApplicationContext(), videoList));
                if(state != null){
                    trailersLv.onRestoreInstanceState(state);
                }

            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch trailers", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void playMedia(String url) {
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        String title = getResources().getString(R.string.app_chooser_title);
        Intent chooser = Intent.createChooser(intent, title);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
