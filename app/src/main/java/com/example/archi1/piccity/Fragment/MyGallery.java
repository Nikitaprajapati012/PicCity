package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.MyGalleryAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.MyGalleryModel;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archi on 20-Mar-17.
 */

public class MyGallery extends android.support.v4.app.Fragment {
    public List<MyGalleryModel> galleryArray;
    public RecyclerView recyclerView;
    private MyGalleryAdapter mAdapter;
    public MyGalleryModel myGalleryModel;
    public ListView lvImageGallery;
    public Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mygallery, container, false);
        utils = new Utils(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_gallery_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (utils.isConnectingToInternet() == true) {

            new getGallery().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.mygallery);
    }

    private class getGallery extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String user_id_gallery;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            galleryArray=new ArrayList<>();
            user_id_gallery = Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web1/pic_citi/Api/get_image_by_user_id.php?user_id=190
            String Url = Constant.Base_URL + "get_image_by_user_id.php?user_id=" + user_id_gallery;
            Log.d("URl", "" + Url);
            return utils.getResponseofGet(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", s);
            try {
                JSONObject mainobject = new JSONObject(s);
                if (mainobject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = mainobject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        myGalleryModel = new MyGalleryModel();
                        myGalleryModel.setId(jsonObject.getString("id"));
                        myGalleryModel.setimage(jsonObject.getString("img"));
                        myGalleryModel.setName(jsonObject.getString("image_name"));
                        myGalleryModel.setPrice(jsonObject.getString("imgprice"));
                        myGalleryModel.setPrice(jsonObject.getString("canvas_image"));
                        galleryArray.add(myGalleryModel);
                    }
                    mAdapter = new MyGalleryAdapter(getActivity(), galleryArray);
                    recyclerView.setAdapter(mAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}