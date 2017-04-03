package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.ArtGalleryDetailsActivity;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;


import java.util.ArrayList;

/**
 * Created by archi1 on 11/26/2016.
 */

public class ArtGalleryAdapter extends BaseAdapter {

    public Context context;
    private ArrayList<ArtGallery> artGalleryArrayList;
    public LayoutInflater inflater;


    public ArtGalleryAdapter(Context context, ArrayList<ArtGallery> artGalleryArrayList) {
        this.context = context;
        this.artGalleryArrayList = artGalleryArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return artGalleryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_art_gallery, null);

        Log.e("Arraydata","" +artGalleryArrayList.size());
        ArtGallery album = artGalleryArrayList.get(position);
        final String pos = artGalleryArrayList.get(position).getId();
        final String strDesc = artGalleryArrayList.get(position).getDescription();
        final String strImage = artGalleryArrayList.get(position).getImage();
        final String strPrice = artGalleryArrayList.get(position).getPrice();
        final String strLocation = artGalleryArrayList.get(position).getLocation();
        final String name = artGalleryArrayList.get(position).getName();
        final String userName=artGalleryArrayList.get(position).getUsername();
        final String  userid= artGalleryArrayList.get(position).getUserId();
        final String currency=artGalleryArrayList.get(position).getCurrency();

        final ImageView artImage = (ImageView) convertView.findViewById(R.id.iv_art_image);
        //TextView tvImgPrice = (TextView)convertView.findViewById(R.id.tv_image_price);
        TextView tvImageTitle = (TextView) convertView.findViewById(R.id.tv_image_title);
        // TextView tvImgLctn = (TextView)convertView.findViewById(R.id.tv_image_location);

        //tvImgPrice.setText(artGalleryArrayList.get(position).getPrice());
        tvImageTitle.setText(album.getName());
        if(!artGalleryArrayList.get(position).getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(artGalleryArrayList.get(position).getImage()).placeholder(R.drawable.ic_placeholder).into(artImage);
        }
      //  Glide.with(context).load(album.getImage()).placeholder(R.drawable.ic_placeholder).into(artImage);
        Log.d("mobile",album.getImage());
        //tvImgLctn.setText(artGalleryArrayList.get(position).getLocation());
        //Picasso.with(context).load(artGalleryArrayList.get(position).getImage()).placeholder(R.drawable.ic_placeholder).into(artImage);


        artImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ArtGalleryDetailsActivity.class);
                i.putExtra("id", pos);
                i.putExtra("description", strDesc);
                i.putExtra("price", strPrice);
                i.putExtra("location", strLocation);
                i.putExtra("image", strImage);
                i.putExtra("username",userName);
                i.putExtra("name", name);
                i.putExtra("userid",userid);
                i.putExtra("currency",currency);
                context.startActivity(i);

                Toast.makeText(context, ""+strLocation, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}
