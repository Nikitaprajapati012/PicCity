package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.CanvasGalleryImage;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.R;

import java.util.List;

/**
 * Created by archi1 on 11/25/2016.
 */

public class CanvasImageListAdapter extends RecyclerView.Adapter<CanvasImageListAdapter.MyViewHolder> {
    private Context mContext;
    private List<GalleryDetails> galleryDetailsList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        GalleryDetails album = galleryDetailsList.get(position);
        final String userId = galleryDetailsList.get(position).getId();
        final String strName = galleryDetailsList.get(position).getName();
        final String strImage = galleryDetailsList.get(position).getImage();
        final String strCanvasImg = galleryDetailsList.get(position).getCanvas_image();
        final String strSizePrice = galleryDetailsList.get(position).getSize();

        holder.glryImageTitle.setText(album.getName());
        Glide.with(mContext).load(album.getCanvas_image()).placeholder(R.drawable.ic_placeholder).into(holder.galleryImage);
        Log.d("CANVAS_IMAGE", "" + album.getCanvas_image());
        Log.d("IMAGE", "" + album.getImage());

        holder.galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CanvasGalleryImage.class);
                intent.putExtra("canvasID", userId);
                intent.putExtra("canvasImageTitle", strName);
                intent.putExtra("canvasImageOriginal", strImage);
                intent.putExtra("canvasImageCanvasType", strCanvasImg);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryDetailsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryImage;
        TextView glryImageTitle;

        MyViewHolder(View view) {
            super(view);
            galleryImage = (ImageView) view.findViewById(R.id.adapter_gallery_image);
            glryImageTitle = (TextView) view.findViewById(R.id.adapter_gallery_image_title);
            galleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public CanvasImageListAdapter(Context mContext, List<GalleryDetails> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = galleryDetailsList;
    }
}
