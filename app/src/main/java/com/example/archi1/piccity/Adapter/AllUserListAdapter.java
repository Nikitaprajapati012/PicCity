package com.example.archi1.piccity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Fragment.PersonalArtistPageFragment;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by archi1 on 4/22/2017.
 */

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.MyViewHolder> {
    public Utils utils;
    private Context mContext;
    private ArrayList<PersonalArtistPage> chatUserLists;
    private String userName, userId, otherUserId;

    public AllUserListAdapter(Context context, ArrayList<PersonalArtistPage> chatUserLists) {
        this.mContext = context;
        this.chatUserLists = chatUserLists;
        this.utils = new Utils(mContext);
        Log.d("size", "" + chatUserLists.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_alluser_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AllUserListAdapter.MyViewHolder holder, final int position) {
        final PersonalArtistPage album = chatUserLists.get(position);
        userName = album.getUserName();
        holder.tvChatUserName.setText(userName);
        Picasso.with(mContext).load(album.getUserImage()).placeholder(R.drawable.ic_placeholder).into(holder.chatProfileCircular);
        if (album.getUserStatus().equalsIgnoreCase("1")) {
            holder.btnSendRequest.setClickable(false);
            holder.btnSendRequest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background));
            holder.btnSendRequest.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.btnSendRequest.setText(R.string.requestsent);
        } else if (album.getUserStatus().equalsIgnoreCase("2")) {
            holder.btnSendRequest.setClickable(true);
            holder.btnSendRequest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.btnSendRequest.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.btnSendRequest.setText(R.string.sendfriendrequest);
        } else if (album.getUserStatus().equalsIgnoreCase("0")){
            holder.btnSendRequest.setClickable(false);
            holder.btnSendRequest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background));
            holder.btnSendRequest.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.btnSendRequest.setText(R.string.friends);
        }

        holder.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = Utils.ReadSharePrefrence(mContext, Constant.USER_ID);
                otherUserId = album.getUserId();
                String isRequestSend = chatUserLists.get(position).getUserStatus();
                if (isRequestSend.equalsIgnoreCase("2")) {
                    chatUserLists.get(position).setUserStatus("1");
                    holder.btnSendRequest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background));
                    holder.btnSendRequest.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                    holder.btnSendRequest.setText(R.string.requestsent);
                } else if (isRequestSend.equalsIgnoreCase("1")){
                    chatUserLists.get(position).setUserStatus("1");
                } else if (isRequestSend.equalsIgnoreCase("0")){
                    chatUserLists.get(position).setUserStatus("0");
                }
                new SendFriendRequest(album, userId, otherUserId, position, isRequestSend).execute();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return chatUserLists.size();
    }

    private class SendFriendRequest extends AsyncTask<String, String, String> {
        String user_id, otheruser_id, task;
        int pos;
        PersonalArtistPage album;
        private ProgressDialog pd;

        private SendFriendRequest(PersonalArtistPage album, String userid, String otheruserid, int position, String task) {
            this.user_id = userid;
            this.otheruser_id = otheruserid;
            this.album = album;
            this.pos = position;
            this.task = task;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mContext);
            pd.setMessage("Request Sending....");
            pd.setCancelable(false);
            pd.isShowing();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://web-medico.com/web1/pic_citi/Api/friend_request.php?sender_id=190&receiver_id=37
            return Utils.getResponseofGet(Constant.Base_URL + "friend_request.php?sender_id=" + user_id + "&receiver_id=" + otheruser_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE!@", "" + s);
            pd.dismiss();
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(mContext, "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    JSONObject secObject = mainObject.getJSONObject("data");
                    // TODO: 5/4/2017 show sender details
                    JSONObject senderObject = secObject.getJSONObject("sender");
                    album = new PersonalArtistPage();
                    album.setId(senderObject.getString("id"));
                    // TODO: 5/4/2017 show receiver details
                    JSONObject receiverObject = secObject.getJSONObject("receiver");
                    album.setId(receiverObject.getString("id"));
                    // TODO: 5/4/2017 show user status
                    if (task.equalsIgnoreCase("2")) {
                        chatUserLists.get(pos).setUserStatus("1");
                    }
                    chatUserLists.add(album);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView chatProfileCircular;
        TextView tvChatUserName;
        LinearLayout layoutUsersList;
        Button btnSendRequest;

        MyViewHolder(View view) {
            super(view);
            chatProfileCircular = (CircleImageView) view.findViewById(R.id.adapter_all_user_imgprofilepic);
            tvChatUserName = (TextView) view.findViewById(R.id.adapter_all_user_txtusername);
            layoutUsersList = (LinearLayout) view.findViewById(R.id.adapter_all_userlist_layout);
            btnSendRequest = (Button) view.findViewById(R.id.adapter_all_user_btnsendrequest);
        }
    }
}
