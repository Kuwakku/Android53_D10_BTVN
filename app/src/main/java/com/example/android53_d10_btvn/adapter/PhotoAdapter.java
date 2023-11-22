package com.example.android53_d10_btvn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android53_d10_btvn.R;
import com.smarteist.autoimageslider.SliderViewAdapter;


import java.util.List;

public class PhotoAdapter extends SliderViewAdapter<PhotoAdapter.PhotoViewHolder> {
    private final List<String> mListPhotos;
    private Context mContext;

    public PhotoAdapter(List<String> mListPhotos) {
        this.mListPhotos = mListPhotos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent) {
        this.mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_slider, null);
        return new PhotoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder viewHolder, int position) {
        final String photo = mListPhotos.get(position);
        // Glide is use to load image
        // from url in your imageview.
        Glide.with(mContext)
                .load(photo)
                .fitCenter()
                .into(viewHolder.imgPhoto);
    }

    @Override
    public int getCount() {
        return mListPhotos.size();
    }

    public class PhotoViewHolder extends ViewHolder {
        View itemView;
        ImageView imgPhoto;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            this.itemView = itemView;
        }
    }
}
