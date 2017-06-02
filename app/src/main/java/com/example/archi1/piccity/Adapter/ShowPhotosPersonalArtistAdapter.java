package com.example.archi1.piccity.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/***Created by Ravi archi on 2/21/2017.
 */

public class ShowPhotosPersonalArtistAdapter extends ArrayAdapter<PersonalArtistPage> {
    private String Id;
    private Utils utils;
    private int resourceId;
    private Dialog dialog;
    private List<PersonalArtistPage> arrayList;
    private Context context;
    private Button btnCancel;
    private ImageView imgPersonalArtist;

    public ShowPhotosPersonalArtistAdapter(Context context,int layoutResourceId,ArrayList<PersonalArtistPage> arraylist) {
        super(context,layoutResourceId,arraylist);
        this.context = context;
        this.resourceId = layoutResourceId;
        this.arrayList = arraylist;
        this.utils=new Utils(context);
        notifyDataSetChanged();
       }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, viewGroup, false);
            holder = new ViewHolder();
            holder.layoutPersonalArtist  = (LinearLayout)row.findViewById(R.id.adapter_video_list_layout);
            holder.imgPersonalArtistPhotos  = (ImageView)row.findViewById(R.id.adapter_video_list_image_view);
            holder.vdPersonalArtistPhotos  = (VideoView)row.findViewById(R.id.adapter_video_list_video_view);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final PersonalArtistPage userdetails = arrayList.get(position);
        Id = userdetails.getId();
        if (userdetails.getImageType().equalsIgnoreCase("1")){
            holder.vdPersonalArtistPhotos.setVisibility(View.GONE);
            holder.imgPersonalArtistPhotos.setVisibility(View.VISIBLE);
           Picasso.with(context).load(userdetails.getImagePath()).placeholder(R.drawable.ic_placeholder).into(holder.imgPersonalArtistPhotos);
        }
        else{
            holder.imgPersonalArtistPhotos.setVisibility(View.GONE);
            holder.vdPersonalArtistPhotos.setVisibility(View.GONE);
            holder.layoutPersonalArtist.setVisibility(View.GONE);
        }
        holder.imgPersonalArtistPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openDialog(userdetails);
            }
        });
        return row;
    }

    // TODO: 4/20/2017 open dialog for sold image
    private void openDialog(final PersonalArtistPage userdetails) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_buy_peronal_artist_image_option);
        dialog.setTitle("");
        btnCancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        imgPersonalArtist = (ImageView) dialog.findViewById(R.id.dialog_image);
        Picasso.with(context).load(userdetails.getImagePath()).placeholder(R.drawable.ic_placeholder).into(imgPersonalArtist);
        dialog.show();
        imgPersonalArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(context, GalleryDetailsActivity.class);
                Gson gson=new Gson();
                i.putExtra("canvaspicdetails",gson.toJson(userdetails));
                context.startActivity(i);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                dialog.dismiss();
            }
        });
    }

    private static class ViewHolder {
        LinearLayout layoutPersonalArtist;
        ImageView imgPersonalArtistPhotos;
        VideoView vdPersonalArtistPhotos;
    }
    }
