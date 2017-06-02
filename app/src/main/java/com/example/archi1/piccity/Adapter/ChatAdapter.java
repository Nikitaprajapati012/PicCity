package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.MessageDetails;
import com.example.archi1.piccity.R;

import java.util.List;

/**
 * Created by archi on 07-Apr-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SENDERVIEW = 0;
    private static final int RECEIVERVIEW = 1;
    private Context context;
    private Utils utils;
    private List<MessageDetails> chatArray;
    private String Receive_id;

    public ChatAdapter(Context context, List<MessageDetails> data) {
        this.context = context;
        utils = new Utils(context);
        this.chatArray = data;
    }



    @Override
    public int getItemViewType(int position) {
        Receive_id = Utils.ReadSharePrefrence(context, Constant.USER_ID);
        if (chatArray.get(position).getSender().equalsIgnoreCase(Utils.ReadSharePrefrence(context, Constant.USER_ID))) {
            return SENDERVIEW;
        } else {//if (chatArray. get(position).getRecipient().equalsIgnoreCase(Utils.ReadSharePrefrence(context, Constant.RECEIPT_ID))){
            return RECEIVERVIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SENDERVIEW:
                View sender = inflater.inflate(R.layout.adapter_senderview, parent, false);
                viewHolder = new SenderViewHolder(sender);
                break;
            case RECEIVERVIEW:
                View receiver = inflater.inflate(R.layout.adapter_receiverview, parent, false);
                viewHolder = new ReceiverViewHolder(receiver);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case SENDERVIEW:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                senderViewHolder.msgTv.setText(chatArray.get(position).getText());
                Log.d("SENDER", "" + chatArray.get(position).getText());
                break;
            case RECEIVERVIEW:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                receiverViewHolder.msgTv.setText(chatArray.get(position).getText());
                Log.d("RECEIVER", "" + chatArray.get(position).getText());
                break;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("@COUNT", "abc" + chatArray.size());
        return chatArray.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;

        public SenderViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_sendview_text);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_receiverview_text);
        }
    }
}