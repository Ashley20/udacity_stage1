package com.example.sahip.moviesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahip.moviesapp.R;
import com.example.sahip.moviesapp.models.Video;

import java.util.ArrayList;
import java.util.List;

public class TrailersAdapter extends ArrayAdapter<Video>{
    private Context context;
    private ArrayList<Video> trailers;

    public TrailersAdapter(Context context, ArrayList<Video> trailers){
        super(context, 0, trailers);
        this.context = context;
        this.trailers = trailers;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.trailers_listview, parent, false);
        }

        ImageView playIv = convertView.findViewById(R.id.play_iv);
        TextView trailerNameTv = convertView.findViewById(R.id.trailer_tw);

        Video video = getItem(position);
        trailerNameTv.setText(video != null ? video.getName() : null);
        playIv.setImageResource(R.drawable.play);


        return convertView;
    }
}
