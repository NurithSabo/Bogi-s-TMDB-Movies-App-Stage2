package com.example.android.bogipopmoviesstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Bogi on 2018. 03. 15..
 */

public class TheMovieAdapter extends RecyclerView.Adapter<TheMovieAdapter.TheMovieViewHolder>{
    private final Context mContext;
    private ArrayList<TheMovie> mFilmList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TheMovieAdapter(Context context, ArrayList<TheMovie> mListOfMovies) {
        mContext = context;
        mFilmList = mListOfMovies;
    }

    @NonNull
    @Override
    public TheMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recy_image, parent, false);
        return new TheMovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TheMovieViewHolder holder, int position) {
        TheMovie currentMovie = mFilmList.get(position);

        String imageUrl;

        if(MainActivity.getOptionOfSort().equals(mContext.getResources().getString(R.string.top_rated))){
            imageUrl = currentMovie.getPosterPath();}
        else if (MainActivity.getOptionOfSort().equals(mContext.getResources().getString(R.string.menu_favorite))){
            imageUrl = currentMovie.getPosterPath2();}
        else {imageUrl = currentMovie.getPosterPath();}


        final int radius = 10;
        final int margin = 10;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);

        Picasso.with(mContext)
                .load(imageUrl)
                .transform(transformation)
                .error(R.drawable.mobile)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    public class TheMovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mImageView;

        public TheMovieViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.keptarto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
