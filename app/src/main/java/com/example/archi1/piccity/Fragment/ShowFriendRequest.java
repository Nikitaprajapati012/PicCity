package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.archi1.piccity.Adapter.UserRequestListAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*** Created by Ravi archi on 1/10/2017.
 */

public class ShowFriendRequest extends Fragment {
    public RecyclerView friendRequestRecyclerView;
    public Utils utils;
    public PersonalArtistPage details;
    public ArrayList<PersonalArtistPage> arrayList;
    public UserRequestListAdapter adapter;
    public String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_videos, container, false);
        friendRequestRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_show_videos);
        init();
        return view;
    }

    // TODO: 4/20/2017  intilization
    private void init() {
        utils=new Utils(getActivity());
        userId= Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
        new GetFriendRequest(userId).execute();

    }
    // TODO: 4/19/2017 show photos in gridview
    private class GetFriendRequest extends AsyncTask<String,String ,String> {
        ProgressDialog pd;
        String userid;

        public GetFriendRequest(String userId) {
            this.userid=userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList=new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/request_list.php?user_id=190
            return Utils.getResponseofGet(Constant.Base_URL + "request_list.php?user_id="+userid);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE Request",""+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                arrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for (int i =0;i<jsonArray.length();i++) {
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
            if(arrayList.size() > 0){
                showFriendRequest();
            }
        }
    }

    // TODO: 5/4/2017 show the list of friend request
    private void showFriendRequest() {
        if (arrayList.size()>0){
            // TODO: 4/19/2017 set adapter for show the the videos
            adapter = new UserRequestListAdapter(getActivity(),arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            friendRequestRecyclerView.setLayoutManager(mLayoutManager);
            friendRequestRecyclerView.setItemAnimator(new DefaultItemAnimator());
            friendRequestRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        new GetFriendRequest(userId).execute();
    }
}
