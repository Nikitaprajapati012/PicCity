package com.example.archi1.piccity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.ShowVideosPersonalArtistAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/*** Created by Ravi archi on 1/10/2017.
 */

public class ShowVideos extends Fragment {
    public String ID,strPersonalArtist;
    public RecyclerView showVideosRecyclerView;
    public Utils utils;
    public PersonalArtistPage details;
    public ArrayList<PersonalArtistPage> arrayList;
    public ShowVideosPersonalArtistAdapter showVideosPersonalArtistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_videos, container, false);
        showVideosRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_show_videos);
        init();
        return view;
    }

    // TODO: 4/20/2017  intilization
    private void init() {
        utils=new Utils(getActivity());
        arrayList=new ArrayList<>();
            Gson gson = new Gson();
            strPersonalArtist = Utils.ReadSharePrefrence(getActivity(), Constant.PERSONAL_ARTIST_PHOTOS);
        if (strPersonalArtist.length() > 0){
            Type type = new TypeToken<ArrayList<PersonalArtistPage>>(){}.getType();
            arrayList= gson.fromJson(strPersonalArtist, type);
        }
        if (arrayList.size()>0){
            // TODO: 4/19/2017 set adapter for show the the videos
            showVideosPersonalArtistAdapter = new ShowVideosPersonalArtistAdapter(getActivity(),arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            showVideosRecyclerView.setLayoutManager(mLayoutManager);
            showVideosRecyclerView.setItemAnimator(new DefaultItemAnimator());
            showVideosRecyclerView.setAdapter(showVideosPersonalArtistAdapter);
            showVideosPersonalArtistAdapter.notifyDataSetChanged();
        }
    }
}
