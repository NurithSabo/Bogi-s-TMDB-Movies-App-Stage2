package com.example.android.bogipopmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bogi on 2018. 04. 11..
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private final Context mContext;
    private final ArrayList<Trailer> trailers;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public TrailersAdapter(Context context, ArrayList<Trailer> mVideoList){
        mContext = context;
        trailers = mVideoList;
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recy_video,parent,false);
        return  new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {

        final Trailer currentTrailer = trailers.get(position);
        String currentTrailerImage = currentTrailer.getDefaultImage();
        String currentTrailerTitle = currentTrailer.getName();

        Picasso
                .with(mContext)
                .load(currentTrailerImage)
                .into(holder.mImageview);

        holder.mTitleView.setText(currentTrailerTitle);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTrailer.getUrl()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public class TrailersViewHolder extends  RecyclerView.ViewHolder {
    final ImageView mImageview;
    final ImageView mPlay;
    final TextView  mTitleView;

    public TrailersViewHolder(View itemView) {
        super(itemView);
        mImageview = (ImageView) itemView.findViewById(R.id.video_preview);
        mTitleView = (TextView) itemView.findViewById(R.id.textview_title);
        mPlay = (ImageView) itemView.findViewById(R.id.imageview_play);

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