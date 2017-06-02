package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.archi1.piccity.Adapter.AllUserListAdapter;
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

public class ShowAllUser extends Fragment {
    public RecyclerView showVideosRecyclerView;
    public Utils utils;
    public PersonalArtistPage details;
    public ArrayList<PersonalArtistPage> arrayList,searchedArraylist;
    public AllUserListAdapter friendListAdapter;
    public EditText searchAllUser;
    public String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_alluser, container, false);
        showVideosRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_show_alluaer);
        searchAllUser=(EditText)view.findViewById(R.id.fragment_show_alluser_search);
        init();
        return view;
    }

    // TODO: 4/20/2017  intilization
    private void init() {
        utils = new Utils(getActivity());
        userId= Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
        new getAllUserList(userId).execute();
        searchAllUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchedArraylist = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getUserName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchedArraylist.add(arrayList.get(i));
                    }
                }
                friendListAdapter = new AllUserListAdapter(getActivity(), searchedArraylist);
                showVideosRecyclerView.setAdapter(friendListAdapter);
            }
        });
    }

    private class getAllUserList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String user_id;

        public getAllUserList(String userId) {
            this.user_id=userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/get_user_list.php?user_id=190
            String Url = Constant.Base_URL +"get_user_list.php?user_id=" + user_id;
            Log.d("URL_AllUser",""+ Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            super.onPostExecute(s);
            Log.d("RESPONSE",s);
            try {
                JSONObject mainobject = new JSONObject(s);
                if (mainobject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = mainobject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        details=new PersonalArtistPage();
                        details.setUserId(jsonObject.getString("id"));
                        details.setUserName(jsonObject.getString("name"));
                        details.setUserImage(jsonObject.getString("user_image"));
                        details.setUserStatus(jsonObject.getString("status"));
                        arrayList.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayList.size()>0){
                // TODO: 4/19/2017 set adapter for show the the videos
                friendListAdapter = new AllUserListAdapter(getActivity(),arrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                showVideosRecyclerView.setLayoutManager(mLayoutManager);
                showVideosRecyclerView.setItemAnimator(new DefaultItemAnimator());
                showVideosRecyclerView.setAdapter(friendListAdapter);
                friendListAdapter.notifyDataSetChanged();
            }
        }
    }
}

