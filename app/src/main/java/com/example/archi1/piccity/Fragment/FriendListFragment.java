package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.archi1.piccity.Adapter.ChatFragmentAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ChatUserList;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 4/22/2017.
*/

public class FriendListFragment extends android.support.v4.app.Fragment {

    public RecyclerView recyclerViewFriendList;
    public ArrayList<ChatUserList> arrayList;
    public ChatFragmentAdapter adapter;
    Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friendlist, container, false);
        utils = new Utils(getActivity());

        recyclerViewFriendList=(RecyclerView)view.findViewById(R.id.fragment_friendlist_recycler_chatpeoplelist);

        //new getChatUserList().execute();
        return view;
    }

    private class getChatUserList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

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
            /*http://web-medico.com/web1/pic_citi/Api/get_image_data.php*/
            String Url = Constant.Base_URL + "get_users_chat.php?user_id="+ Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);//Constant.USER_ID;
            Log.d("URL_CHAT",""+ Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("CHATLOG.",s);
            try {
                JSONObject mainobject = new JSONObject(s);

                if (mainobject.getString("status").equalsIgnoreCase("true")) {

                    JSONArray jsonArray = mainobject.getJSONArray("receiver");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ChatUserList gallery = new ChatUserList();
                        gallery.setChatUserId(jsonObject.getString("id"));
                        gallery.setChatUserName(jsonObject.getString("name"));
                        arrayList.add(gallery);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new ChatFragmentAdapter(getActivity(), arrayList);
            recyclerViewFriendList.setAdapter(adapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewFriendList.setLayoutManager(mLayoutManager);
            recyclerViewFriendList.setItemAnimator(new DefaultItemAnimator());
            recyclerViewFriendList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pd.dismiss();
        }
    }
}
