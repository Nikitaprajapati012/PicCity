package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.R;

import java.util.List;

/**
 * Created by archi1 on 11/25/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context mContext;
    private List<GalleryDetails> galleryDetailsList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        GalleryDetails album = galleryDetailsList.get(position);
        final String pos = galleryDetailsList.get(position).getId();
        final String strTitle = galleryDetailsList.get(position).getName();
        final String strImage = galleryDetailsList.get(position).getImage();
        final String strCanvasImg = galleryDetailsList.get(position).getCanvas_image();
        final String strSizePrice = galleryDetailsList.get(position).getSize();

        holder.glryImageTitle.setText(album.getName());
        Glide.with(mContext).load(album.getCanvas_image()).placeholder(R.drawable.ic_placeholder).into(holder.galleryImage);

        holder.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.menuImageView);
            }
        });

        holder.galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GalleryDetailsActivity.class);
                i.putExtra("id", pos);
                i.putExtra("title", strTitle);
                i.putExtra("image", strImage);
                i.putExtra("canvasImage", strCanvasImg);
                i.putExtra("sizePrice", strSizePrice);
                mContext.startActivity(i);

            }
        });
    }

    private void showPopupMenu(ImageView menuImageView) {

        PopupMenu popupMenu = new PopupMenu(mContext, menuImageView);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_album, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }


    @Override
    public int getItemCount() {
        return galleryDetailsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryImage;
        TextView glryImageTitle;
        ImageView menuImageView;


        MyViewHolder(View view) {
            super(view);
            galleryImage = (ImageView) view.findViewById(R.id.adapter_gallery_image);
            glryImageTitle = (TextView) view.findViewById(R.id.adapter_gallery_image_title);
            menuImageView = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public GalleryAdapter(Context mContext, List<GalleryDetails> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = galleryDetailsList;
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.adapter_gallery_image_send_my_gallery:
                    Toast.makeText(mContext, "send my gallery", Toast.LENGTH_SHORT).show();
                    return true;


                case R.id.adapter_gallery_image_canvas_gallery_pic:
                    Toast.makeText(mContext, "canvas gallery pic", Toast.LENGTH_SHORT).show();
                    return true;


                case R.id.adapter_gallery_image_paste_me:

                    return true;
                default:
            }
            return false;
        }
    }
}
