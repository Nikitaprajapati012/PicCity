package com.example.archi1.piccity.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.archi1.piccity.Fragment.ShowPhotos;
import com.example.archi1.piccity.Fragment.ShowVideos;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;


public class PersoinalArtistProfilePager extends FragmentStatePagerAdapter {
    public Fragment fragment;
    int tabCount;
    public PersonalArtistPage artistPageDetails;
    public Context mContext;

    public PersoinalArtistProfilePager(FragmentActivity activity, PersonalArtistPage details, FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.artistPageDetails=details;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = null;
        Gson gson =new Gson();
        Bundle bundle = new Bundle();
        bundle.putString("friendlistdetails",gson.toJson(artistPageDetails));
        switch (position) {
            case 0:
                fragment = new ShowPhotos();
                break;
            case 1:
                fragment = new ShowVideos();
                break;
            default:
                return null;

        }
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}