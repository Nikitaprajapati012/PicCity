package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.archi1.piccity.Activity.MainActivity;
import com.example.archi1.piccity.Adapter.FriendListAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Ravi archi on 1/10/2017.
 */

public class ShowFriendList extends Fragment {
    public String userId,strImgProfile,userName;
    public RecyclerView showFriendListRecyclerView;
    public Utils utils;
    public PersonalArtistPage details;
    public ArrayList<PersonalArtistPage> arrayList;
    public FriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friendlist, container, false);
        showFriendListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_friendlist_recycler_chatpeoplelist);
        init();
        return view;
    }


    // TODO: 4/20/2017  intilization
    private void init() {
        utils = new Utils(getActivity());
        userId= Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
        new GetFriendList(userId).execute();
    }

    // TODO: 4/19/2017 show photos in gridview
    private class GetFriendList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String userid;

        public GetFriendList(String userId) {
            this.userid = userId;
        }
            @Override
            protected void onPreExecute () {
                super.onPreExecute();
                arrayList = new ArrayList<>();
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
            }
        @Override
        protected String doInBackground(String... strings) {
            // http://web-medico.com/web1/pic_citi/Api/friend_list.php?user_id=190
            return Utils.getResponseofGet(Constant.Base_URL + "friend_list.php?user_id=" + userid);
        }
            @Override
            protected void onPostExecute (String s) {
                super.onPostExecute(s);
                Log.d("RESPONSE Request", "" + s);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    arrayList = new ArrayList<>();
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            details = new PersonalArtistPage();
                            JSONObject object = jsonArray.getJSONObject(i);
                            details.setUserId(object.getString("user_id"));
                            details.setUserName(object.getString("name"));
                            details.setUserEmail(object.getString("email"));
                            details.setUserContactNumber(object.getString("phone"));
                            details.setUserPayPalEmail(object.getString("paypal_email"));
                            details.setUserImage(object.getString("user_image"));
                            details.setUserStatus(object.getString("status"));
                            arrayList.add(details);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (arrayList.size() > 0) {
                    showFriendList();
                }
            }
    }


    // TODO: 5/8/2017 show the list of friends
    private void showFriendList() {
        // TODO: 4/19/2017 set adapter for show the the videos
        adapter = new FriendListAdapter(getActivity(),arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        showFriendListRecyclerView.setLayoutManager(mLayoutManager);
        showFriendListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        showFriendListRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
