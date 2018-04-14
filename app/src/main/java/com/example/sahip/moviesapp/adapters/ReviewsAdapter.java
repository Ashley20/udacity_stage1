package com.example.sahip.moviesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sahip.moviesapp.R;
import com.example.sahip.moviesapp.models.Review;
import com.example.sahip.moviesapp.models.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class ReviewsAdapter extends ArrayAdapter<Review> {
    private Context context;
    private ArrayList<Review> reviewList;

    public ReviewsAdapter(Context context, ArrayList<Review> reviewList){
        super(context,0, reviewList);
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.reviews_listview, parent, false);
        }

        TextView reviewAuthorTv = convertView.findViewById(R.id.review_author_tw);
        TextView reviewContentTv =  convertView.findViewById(R.id.review_content_tw);

        Review review = getItem(position);
        reviewAuthorTv.setText(review != null ? review.getAuthor() : null);
        reviewContentTv.setText(review != null ? review.getContent() : null);

        return convertView;
    }
}
