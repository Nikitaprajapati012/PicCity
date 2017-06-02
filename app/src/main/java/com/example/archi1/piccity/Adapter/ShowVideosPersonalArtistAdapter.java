package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;

import java.util.ArrayList;
import java.util.List;

/*** Created by Ravi archi on 2/21/2017.
 */

public class ShowVideosPersonalArtistAdapter extends RecyclerView.Adapter<ShowVideosPersonalArtistAdapter.MyViewHolder> {
    public String Id;
    public Utils utils;
    private List<PersonalArtistPage> arrayList;
    private Context context;

    public ShowVideosPersonalArtistAdapter(Context context, ArrayList<PersonalArtistPage> arraylist) {
        this.context = context;
        this.arrayList = arraylist;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_video_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PersonalArtistPage userdetails = arrayList.get(position);
        Id = userdetails.getId();
            if (userdetails.getImageType().equalsIgnoreCase("2")) {
                holder.vdPersonalArtistVideos.setVisibility(View.VISIBLE);
                holder.vdPersonalArtistVideos.setVideoURI(Uri.parse(userdetails.getImagePath()));
                holder.vdPersonalArtistVideos.setMediaController(new MediaController(context));
                holder.vdPersonalArtistVideos.requestFocus();
                holder.vdPersonalArtistVideos.start();
            }
        else
            {
                holder.vdPersonalArtistVideos.setVisibility(View.GONE);
                holder.imgPersonalArtistVideos.setVisibility(View.GONE);
                holder.layoutPersonalArtist.setVisibility(View.GONE);
            }
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
        LinearLayout layoutPersonalArtist;
        VideoView vdPersonalArtistVideos;
        ImageView imgPersonalArtistVideos;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutPersonalArtist  = (LinearLayout)itemView.findViewById(R.id.adapter_video_list_layout);
            vdPersonalArtistVideos  = (VideoView) itemView.findViewById(R.id.adapter_video_list_video_view);
            imgPersonalArtistVideos  = (ImageView) itemView.findViewById(R.id.adapter_video_list_image_view);
        }
    }
}
