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

public class ChatFragment extends android.support.v4.app.Fragment {

    public RecyclerView recyclerView_chatpeople;
    public ArrayList<ChatUserList> chatUserLists;
    public ChatFragmentAdapter chatFragmentAdapter;
    Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chatpoeple, container, false);
        utils = new Utils(getActivity());

        recyclerView_chatpeople=(RecyclerView)view.findViewById(R.id.fragment_chatpeople_recycler_chatpeoplelist);

        new getChatUserList().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.chat);
    }


    private class getChatUserList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chatUserLists = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_image_data.php*/
            String Url = Constant.Base_URL + "get_users_chat.php?user_id="+Utils.ReadSharePrefrence(getActivity(),Constant.USER_ID);//Constant.USER_ID;
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
                        chatUserLists.add(gallery);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chatFragmentAdapter = new ChatFragmentAdapter(getActivity(), chatUserLists);
            recyclerView_chatpeople.setAdapter(chatFragmentAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_chatpeople.setLayoutManager(mLayoutManager);
            recyclerView_chatpeople.setItemAnimator(new DefaultItemAnimator());
            recyclerView_chatpeople.setAdapter(chatFragmentAdapter);
            chatFragmentAdapter.notifyDataSetChanged();
            pd.dismiss();
        }
    }
}
