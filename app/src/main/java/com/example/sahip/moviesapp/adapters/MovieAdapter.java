package com.example.sahip.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sahip.moviesapp.DetailActivity;
import com.example.sahip.moviesapp.R;
import com.example.sahip.moviesapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sahip on 25.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String IMAGE_URL_BASE_PATH = "http://image.tmdb.org/t/p/w185//";
    private Context mContext;
    private List<Movie> mData;


    public MovieAdapter(Context mContext, List<Movie> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        view = mLayoutInflater.inflate(R.layout.cardview_item_movie, parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        final String image_url = IMAGE_URL_BASE_PATH + mData.get(position).getPosterPath();

        Picasso.with(mContext)
                .load(image_url)
                .into(holder.moviePosterIv);


        holder.moviesCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);

                intent.putExtra(DetailActivity.MOVIE_ID, mData.get(position).getId());
                intent.putExtra(DetailActivity.MOVIE_ORIGINAL_TITLE, mData.get(position).getOriginalTitle());
                intent.putExtra(DetailActivity.MOVIE_IMAGE_URL, mData.get(position).getPosterPath());
                intent.putExtra(DetailActivity.MOVIE_VOTE_AVERAGE, mData.get(position).getVoteAverage());
                intent.putExtra(DetailActivity.MOVIE_OVERVIEW, mData.get(position).getOverview());
                intent.putExtra(DetailActivity.MOVIE_RELEASE_DATE, mData.get(position).getReleaseDate());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePosterIv;
        CardView moviesCv;
        
        public MovieViewHolder(View itemView) {
            super(itemView);
            
            moviePosterIv = itemView.findViewById(R.id.movie_poster_image_thumbnail_iv);
            moviesCv = itemView.findViewById(R.id.movies_cv);
        }
    }
}
