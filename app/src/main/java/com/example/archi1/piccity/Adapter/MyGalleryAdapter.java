package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Activity.MainActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.Model.MyGalleryModel;
import com.example.archi1.piccity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.archi1.piccity.R.id.imageView;

/**
 * Created by archi on 30-Mar-17.
 */

public class MyGalleryAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<MyGalleryModel> galleryDetailsList;
    public LayoutInflater inflater;
    protected Utils utils;

    @Override
    public int getCount() {
        return galleryDetailsList.size();
    }

    public MyGalleryAdapter(Context mContext, List<MyGalleryModel> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = (ArrayList<MyGalleryModel>) galleryDetailsList;
        this.utils = new Utils(mContext);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        MyGalleryModel mygallerymodel = galleryDetailsList.get(position);
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.adapter_mygallery, parent, false);
            ImageView ivSetImage = (ImageView) convertView.findViewById(R.id.adapter_mygallery_image);
            Log.d("img", mygallerymodel.getImg());
            Picasso.with(mContext).load(mygallerymodel.getImg()).placeholder(R.drawable.ic_placeholder).into(ivSetImage);
        }
        return convertView;
    }
}