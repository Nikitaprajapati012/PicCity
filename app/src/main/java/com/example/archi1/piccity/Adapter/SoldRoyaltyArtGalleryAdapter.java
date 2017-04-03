package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Model.SoldRoyaltyGallery;
import com.example.archi1.piccity.R;

import java.util.ArrayList;

/**
 * Created by archi1 on 12/7/2016.
 */

public class SoldRoyaltyArtGalleryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<SoldRoyaltyGallery> soldRoyaltyGalleryArrayList;
    public LayoutInflater inflater;


    public SoldRoyaltyArtGalleryAdapter(Context context, ArrayList<SoldRoyaltyGallery> soldRoyaltyGalleryArrayList){
        this.context = context;
        this.soldRoyaltyGalleryArrayList = soldRoyaltyGalleryArrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return soldRoyaltyGalleryArrayList.size();
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

        convertView = inflater.inflate(R.layout.adapter_sold_royalty_art_gallery,null);

        SoldRoyaltyGallery soldRoyaltyAlbum = soldRoyaltyGalleryArrayList.get(position);
        final String pos = soldRoyaltyGalleryArrayList.get(position).getId();
        final String strUserName = soldRoyaltyGalleryArrayList.get(position).getUser_name();
        final  String strUserId = soldRoyaltyGalleryArrayList.get(position).getUser_id();
        final String strImage = soldRoyaltyGalleryArrayList.get(position).getImage();
        final String strImageName = soldRoyaltyGalleryArrayList.get(position).getName();
        final String strImagePrice = soldRoyaltyGalleryArrayList.get(position).getPrice();
        final String strDesc = soldRoyaltyGalleryArrayList.get(position).getDescription();
        final String strLocation = soldRoyaltyGalleryArrayList.get(position).getLocation();

        ImageView soldRoyaltyImage = (ImageView)convertView.findViewById(R.id.adapter_grid_item_sold_royaltyimage);
        TextView soldRoyaltyImageTitle =(TextView)convertView.findViewById(R.id.adapter_grid_item_sold_royaltytitle);
        soldRoyaltyImageTitle.setText(soldRoyaltyGalleryArrayList.get(position).getName());
        Glide.with(context).load(soldRoyaltyGalleryArrayList.get(position).getImage()).placeholder(R.drawable.ic_placeholder).into(soldRoyaltyImage);


        return convertView;
    }
}
