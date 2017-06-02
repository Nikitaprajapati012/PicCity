package com.example.archi1.piccity.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.MyGalleryModel;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by archi on 30-Mar-17.
 */

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyGalleryModel> galleryDetailsList;
    public LayoutInflater inflater;
    protected Utils utils;
    private ImageView ivOption;
    private TextView tvImageName;
    private Dialog dialog;
    private String strImage, strImageId, strImageName;
    private Bitmap rotatedBitmap;
    private MyGalleryModel album;



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mygallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        album = galleryDetailsList.get(position);
        holder.glryImageTitle.setText(album.getName());
        Glide.with(mContext).load(album.getCanvasImage()).placeholder(R.drawable.ic_placeholder).into(holder.galleryImage);
        holder.menuImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                strImage = album.getimage();
                showpopupmenu(holder.menuImageView);
            }
        });
    }

    private void showpopupmenu(ImageView menuImageView) {
       // Log.d("Position:", ""+);
        PopupMenu popupMenu = new PopupMenu(mContext, menuImageView);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_mygallery, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+galleryDetailsList.size());
        return galleryDetailsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryImage;
        TextView glryImageTitle;
        ImageView menuImageView;


        MyViewHolder(View view) {
            super(view);
            galleryImage = (ImageView) view.findViewById(R.id.adapter_mygallery_image);
            glryImageTitle = (TextView) view.findViewById(R.id.adapter_mygallery_image_title);
            menuImageView = (ImageView) view.findViewById(R.id.mygallery_overflow);
            galleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
    public MyGalleryAdapter(Context mContext, List<MyGalleryModel> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = galleryDetailsList;
    }
    private class MyMenuItemClickListener implements OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.adapter_paste_me:
                   break;

                case R.id.adapter_share_fb:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image via");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello...." + " " + strImage);
                        Bundle facebookBundle = new Bundle();
                        facebookBundle.putString(Intent.EXTRA_TEXT, strImage);
                        Bundle replacement = new Bundle();
                        replacement.putBundle("com.facebook.katana", facebookBundle);
                        chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
                    } else {
                        shareIntent.putExtra(Intent.EXTRA_TEXT, strImage);
                    }
                    chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(chooserIntent);
                    break;


                case R.id.canvas_image:
                    Intent i = new Intent(mContext, GalleryDetailsActivity.class);
                    Gson gson=new Gson();
                    i.putExtra("canvaspicdetails",gson.toJson(album));
                    mContext.startActivity(i);
                default:
            }
            return false;
        }
    }
}
