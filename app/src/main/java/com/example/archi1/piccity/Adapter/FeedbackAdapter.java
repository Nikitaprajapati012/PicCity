package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by archi1 on 5/1/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {
    private Context mContext;
    private Utils utils;
    private ArrayList<ArtGallery> arrayList;

    public FeedbackAdapter(Context mContext, ArrayList<ArtGallery> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.utils=new Utils(mContext);
        Log.d("length",""+arrayList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_feedback, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ArtGallery details = arrayList.get(position);
        String imageId=details.getId();
        holder.tvFeed.setText(details.getImageFeedBackText());
        holder.tvUserName.setText(details.getUsername());
        Picasso.with(mContext).load(details.getUserProfilePic()).placeholder(R.drawable.ic_placeholder).into(holder.adapter_ProfileImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView adapter_ProfileImage;
        TextView tvUserName, tvFeed;

        MyViewHolder(View view) {
            super(view);
            adapter_ProfileImage = (CircleImageView) view.findViewById(R.id.adapter_feedback_username_profile);
            tvUserName = (TextView) view.findViewById(R.id.adapter_feedback_username);
            tvFeed = (TextView) view.findViewById(R.id.adapter_feedback_des);
        }
    }
}
