package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.archi1.piccity.Adapter.SoldRoyaltyArtGalleryAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.SoldRoyaltyGallery;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 12/1/2016.
 */

public class SoldFragment extends Fragment {

    public ArrayList<SoldRoyaltyGallery> soldRoyaltyGalleryArrayList;
    public SoldRoyaltyArtGalleryAdapter soldRoyaltyArtGalleryAdapter;
    public Utils utils;
    public GridView royaltySoldProductGridview;
    public String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sold,container,false);

        utils = new Utils(getActivity());
        userId=utils.ReadSharePrefrence(getActivity(), Constant.UserId);
        royaltySoldProductGridview = (GridView)view.findViewById(R.id.fragment_sold_product_royalty_gridview);


        new soldImage().execute();
       // ((Activity) getActivity()).setTitle("Sold");

      return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    // ((Activity) getActivity()).setTitle("Sold");


    }


    private class soldImage extends AsyncTask<String,String ,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
            soldRoyaltyGalleryArrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_sold_item.php?user_id=51*/
            String Url =Constant.Base_URL+"get_sold_item.php?user_id="+userId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");

                    for (int i =0;i<jsonArray.length();i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        SoldRoyaltyGallery soldRoyaltyGallery = new SoldRoyaltyGallery();
                        soldRoyaltyGallery.setId(object.getString("id"));
                        soldRoyaltyGallery.setUser_name(object.getString("user_name"));
                        soldRoyaltyGallery.setUser_id(object.getString("user_id"));
                        soldRoyaltyGallery.setImage(object.getString("image"));
                        soldRoyaltyGallery.setName(object.getString("name"));
                        soldRoyaltyGallery.setPrice(object.getString("price"));
                        soldRoyaltyGallery.setDescription(object.getString("description"));
                        soldRoyaltyGallery.setLocation(object.getString("location"));

                        soldRoyaltyGalleryArrayList.add(soldRoyaltyGallery);
                    }

                    soldRoyaltyArtGalleryAdapter = new SoldRoyaltyArtGalleryAdapter(getActivity(),soldRoyaltyGalleryArrayList);
                    royaltySoldProductGridview.setAdapter(soldRoyaltyArtGalleryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
}

