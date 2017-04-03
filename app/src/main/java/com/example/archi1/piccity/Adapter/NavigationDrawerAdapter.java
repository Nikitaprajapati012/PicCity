package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.archi1.piccity.Model.NavDrawerItem;
import com.example.archi1.piccity.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by archi1 on 11/24/2016.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private int[] textureArrayWin = {
            R.drawable.ic_gallery,
            R.drawable.ic_camera,
            R.drawable.mygallery,
            R.drawable.ic_recipie,
            R.drawable.ic_alist,
            R.drawable.ic_fevorit,
            R.drawable.ic_canvasme,
            R.drawable.ic_canvasmypic,
            R.drawable.ic_logout,
            R.drawable.ic_invite,
    };
    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.title.setTextColor(Color.GRAY);
        holder.imageViewMenu.setImageDrawable(context.getResources().getDrawable(textureArrayWin[position]));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageViewMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageViewMenu= (ImageView) itemView.findViewById(R.id.imageViewMenu);
        }
    }
}