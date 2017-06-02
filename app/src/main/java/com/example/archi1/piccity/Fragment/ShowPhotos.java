package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.archi1.piccity.Activity.MainActivity;
import com.example.archi1.piccity.Adapter.ShowPhotosPersonalArtistAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.archi1.piccity.Constant.Constant.PERSONAL_ARTIST_PHOTOS;

/**
 * Created by archi1 on 12/1/2016.
 */

public class ShowPhotos extends Fragment {
    public ArrayList<PersonalArtistPage> showPhotosPersonalArtistArrayList;
    public ShowPhotosPersonalArtistAdapter showPhotosPersonalArtistAdapter;
    public Utils utils;
    public GridView showPhotosGridView;
    public String userId,artistId;
    public PersonalArtistPage personalArtistPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_photos,container,false);
        utils = new Utils(getActivity());
        userId= Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
        showPhotosGridView = (GridView) view.findViewById(R.id.fragment_show_photos_gridview);
        personalArtistPage = new PersonalArtistPage();
        if (getArguments() != null){
            Gson gson=new Gson();
            String strDetails=getArguments().getString("friendlistdetails");
            personalArtistPage= gson.fromJson(strDetails,PersonalArtistPage.class);
            artistId =personalArtistPage.getUserId();
            Log.d("artistId","@@"+artistId);
            new showPhotos(artistId).execute();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new showPhotos(artistId).execute();
    }

    // TODO: 4/19/2017 show photos in gridview
    private class showPhotos extends AsyncTask<String,String ,String> {
        ProgressDialog pd;
        String artistid;

        public showPhotos(String userId) {
            this.artistid=userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
           //http://web-medico.com/web1/pic_citi/Api/get_parsonal_artist.php?user_id=86
            return Utils.getResponseofGet(Constant.Base_URL + "get_parsonal_artist.php?user_id="+artistid);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE",""+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                showPhotosPersonalArtistArrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for (int i =0;i<jsonArray.length();i++) {
                        personalArtistPage = new PersonalArtistPage();
                        JSONObject object = jsonArray.getJSONObject(i);
                        personalArtistPage.setUserId(object.getString("user_id"));
                        personalArtistPage.setUserName(object.getString("user_name"));
                        personalArtistPage.setUserImage(object.getString("user_image"));
                        personalArtistPage.setUserContactNumber(object.getString("user_phone"));
                        personalArtistPage.setId(object.getString("id"));
                        personalArtistPage.setImageType(object.getString("type"));
                        if (object.getString("type").equalsIgnoreCase("1")) {
                            personalArtistPage.setImagePath(object.getString("image_video"));
                        } else if (object.getString("type").equalsIgnoreCase("2")) {
                            personalArtistPage.setImagePath(object.getString("image_video"));
                        }
                        showPhotosPersonalArtistArrayList.add(personalArtistPage);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(showPhotosPersonalArtistArrayList.size() > 0){
                openPhotosList();
            }
        }
    }

    // TODO: 4/19/2017 open photos list in view
    private void openPhotosList() {
        // TODO: 4/19/2017 store the list in shared prefrence
        Gson gson = new Gson();
        String strPersonalArtisPhotos = gson.toJson(showPhotosPersonalArtistArrayList);
        Utils.WriteSharePrefrence(getActivity(), PERSONAL_ARTIST_PHOTOS,strPersonalArtisPhotos);

        // TODO: 4/19/2017 set adapter for show the the photos
        showPhotosPersonalArtistAdapter = new ShowPhotosPersonalArtistAdapter(getActivity(), R.layout.adapter_video_list,showPhotosPersonalArtistArrayList);
        showPhotosGridView.setAdapter(showPhotosPersonalArtistAdapter);
        showPhotosPersonalArtistAdapter.notifyDataSetChanged();
    }
}

