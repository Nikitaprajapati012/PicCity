package com.example.archi1.piccity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

/*** Created by archi1 on 4/22/2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<PersonalArtistPage> chatUserLists;
    private String userName,userId,otherUserId;
    public Utils utils;

    public FriendListAdapter(Context context, ArrayList<PersonalArtistPage> chatUserLists) {
        this.mContext = context;
        this.chatUserLists = chatUserLists;
        this.utils = new Utils(mContext);
        notifyDataSetChanged();
        Log.d("size", "" + chatUserLists.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_friends_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendListAdapter.MyViewHolder holder, final int position) {
        final PersonalArtistPage album = chatUserLists.get(position);
        final int newPosition = holder.getAdapterPosition();
        userName = album.getUserName();
        userId= Utils.ReadSharePrefrence(mContext, Constant.USER_ID);
        otherUserId=album.getUserId();
        holder.tvChatUserName.setText(userName);
        Picasso.with(mContext).load(album.getUserImage()).placeholder(R.drawable.ic_placeholder).into(holder.friendsProfileCircular);
        holder.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (album.getUserStatus().equalsIgnoreCase("0")) {
                    chatUserLists.get(position).setUserStatus("2");
                    /*holder.btnSendRequest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                    holder.btnSendRequest.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    holder.btnSendRequest.setText(R.string.sendfriendrequest);
                    */new UnFriendFriends(album,userId,otherUserId,position).execute();
                    chatUserLists.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, chatUserLists.size());
                }
                          }
        });

        holder.layoutFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PersonalArtistPageFragment();
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("friendlistdetails",gson.toJson(album));
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_body, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }
    private class UnFriendFriends extends AsyncTask<String,String,String>
    {
        private ProgressDialog pd;
        String user_id,otheruser_id;
        int pos;
        PersonalArtistPage album;

        private UnFriendFriends(PersonalArtistPage album, String userid, String otheruserid, int position) {
            this.user_id=userid;
            this.otheruser_id=otheruserid;
            this.album=album;
            this.pos=position;
        }

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(mContext);
            pd.setMessage("Request Sending....");
            pd.setCancelable(false);
            pd.isShowing();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://web-medico.com/web1/pic_citi/Api/friend_request.php?sender_id=190&receiver_id=37
            return Utils.getResponseofGet(Constant.Base_URL+"friend_request.php?sender_id="+user_id+"&receiver_id="+otheruser_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE!@","@@"+s);
            pd.dismiss();
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true"))
                {
                    JSONObject secObject = mainObject.getJSONObject("data");
                    // TODO: 5/4/2017 show sender details
                    JSONObject senderObject = secObject.getJSONObject("sender");
                    album = new PersonalArtistPage();
                    album.setId(senderObject.getString("id"));
                    album.setUserName(senderObject.getString("name"));
                    album.setUserEmail(senderObject.getString("email"));
                    album.setUserContactNumber(senderObject.getString("phone"));
                    album.setUserImage(senderObject.getString("image"));
                    // TODO: 5/4/2017 show user status
                    album.setUserStatus(secObject.getString("status"));
                    // TODO: 5/4/2017 show receiver details
                    JSONObject receiverObject = secObject.getJSONObject("receiver");
                    album.setId(receiverObject.getString("id"));
                    album.setUserName(receiverObject.getString("name"));
                    album.setUserEmail(receiverObject.getString("email"));
                    album.setUserContactNumber(receiverObject.getString("phone"));
                    album.setUserImage(receiverObject.getString("image"));
                    chatUserLists.add(album);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        CircleImageView friendsProfileCircular;
        TextView tvChatUserName;
        LinearLayout layoutFriendList;
        Button btnSendRequest;

        MyViewHolder(View view) {
            super(view);
            friendsProfileCircular = (CircleImageView)view.findViewById(R.id.adapter_friends_user_imgprofilepic);
            tvChatUserName = (TextView)view.findViewById(R.id.adapter_friends_user_txtusername);
            layoutFriendList = (LinearLayout)view.findViewById(R.id.adapter_friends_userlist_layout);
            btnSendRequest = (Button) view.findViewById(R.id.adapter_friends_user_btnsendrequest);
        }
    }
}
