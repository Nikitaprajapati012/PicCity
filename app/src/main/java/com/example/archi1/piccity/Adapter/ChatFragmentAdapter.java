package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Activity.ChatActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ChatUserList;
import com.example.archi1.piccity.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by archi1 on 4/22/2017.
 */

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ChatUserList> chatUserLists;
    private String userName;
    public Utils utils;

    public ChatFragmentAdapter(Context context, ArrayList<ChatUserList> chatUserLists) {
        this.mContext = context;
        this.chatUserLists = chatUserLists;
        this.utils=new Utils(mContext);
        Log.d("size",""+chatUserLists.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chatpeople_list, parent, false);
       return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatFragmentAdapter.MyViewHolder holder, final int position) {
        final ChatUserList album = chatUserLists.get(position);
        userName = album.getChatUserName();
        holder.tvChatUserName.setText(userName);
        Toast.makeText(mContext, ""+album.getChatUserId(), Toast.LENGTH_SHORT).show();
        holder.layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ichat =new Intent(mContext, ChatActivity.class);
                ichat.putExtra("sender_id", Utils.ReadSharePrefrence(mContext,Constant.USER_ID));
                ichat.putExtra("receipt_id",album.getChatUserId());
                mContext.startActivity(ichat);
            }
        });
       // Glide.with(mContext).load(album.getCanvas_image()).placeholder(R.drawable.ic_placeholder).into(holder.galleryImage);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return chatUserLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
         CircleImageView chatProfileCircular;
         TextView tvChatUserName;
        LinearLayout layoutChat;

        MyViewHolder(View view) {
            super(view);
            chatProfileCircular = (CircleImageView)view.findViewById(R.id.adapter_chatpeople_list_imgprofilepic);
            tvChatUserName = (TextView)view.findViewById(R.id.adapter_chatpeople_list_txtusername);
            layoutChat = (LinearLayout)view.findViewById(R.id.adapter_chatpeople_list_layout);
        }
    }
}
