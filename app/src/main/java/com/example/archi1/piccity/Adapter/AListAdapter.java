package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Fragment.AList;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.archi1.piccity.R.id.imageView;
import static com.example.archi1.piccity.R.id.thing_proto;

/**
 * Created by archi1 on 12/23/2016.
 */

public class AListAdapter extends BaseAdapter {

    public Context context;
    public LayoutInflater inflater;
    public FragmentActivity activity;
    private List<String> friendList= new ArrayList<>();



    public AListAdapter(Context context, List<String> friendList) {
        this.context = context;
        this.friendList = friendList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_a_list,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.Name.setText(friendList.get(position));
        //holder.imageView.setImageDrawable(R.drawable.ic_placeholder);
        Glide.with(context).load(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(holder.imageView);

        return convertView;
    }

 /*   public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        friendList.clear();
        if (charText.length() == 0) {
            friendList.addAll(searchList);
        } else {
            for (String s : searchList) {
                if (s.toLowerCase(Locale.getDefault()).contains(charText)) {
                    friendList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    private class ViewHolder {
        private ImageView imageView;
        private TextView Name;

        ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imageView);
            Name = (TextView) v.findViewById(R.id.text);
        }
    }
}
