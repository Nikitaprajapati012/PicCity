package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.ArtGalleryDetailsActivity;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*** Created by Ravi archi on 2/21/2017.
 */

public class ArtGalleryAdapter extends RecyclerView.Adapter<ArtGalleryAdapter.MyViewHolder> {
    public String Id;
    public Utils utils;
    private List<ArtGallery> arrayList;
    private Context mContext;

    public ArtGalleryAdapter(Context context, ArrayList<ArtGallery> arraylist) {
        this.mContext = context;
        this.arrayList = arraylist;
        Log.d("Length","@"+arrayList.size());
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_categorydetails_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ArtGallery details = arrayList.get(position);
        Id = details.getId();
        holder.txtName.setText(details.getName());
        holder.txtDescription.setText(details.getDescription());
        holder.txtPrice.setText(details.getPrice());
        if (details.getImage().length() > 0){
            Picasso.with(mContext).load(details.getImage()).placeholder(R.drawable.ic_placeholder).into(holder.imgArtGallery);
        }
        else {Picasso.with(mContext).load(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(holder.imgArtGallery);
            }
            holder.layoutArtGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ArtGalleryDetailsActivity.class);
                    Gson gson=new Gson();
                    intent.putExtra("artgallerydetails",gson.toJson(details));
                    mContext.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutArtGallery;
        ImageView imgArtGallery;
        TextView txtDescription,txtPrice,txtName;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutArtGallery  = (LinearLayout)itemView.findViewById(R.id.adapter_categorydetails_list_layout);
            imgArtGallery  = (ImageView) itemView.findViewById(R.id.adapter_categorydetails_list_img);
            txtDescription  = (TextView) itemView.findViewById(R.id.adapter_categorydetails_list_txtdescription);
            txtPrice  = (TextView) itemView.findViewById(R.id.adapter_categorydetails_list_txtprice);
            txtName  = (TextView) itemView.findViewById(R.id.adapter_categorydetails_list_txtimagename);
        }
    }
}
