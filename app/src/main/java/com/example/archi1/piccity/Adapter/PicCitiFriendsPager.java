package com.example.archi1.piccity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.archi1.piccity.Fragment.ShowAllUser;
import com.example.archi1.piccity.Fragment.ShowFriendList;
import com.example.archi1.piccity.Fragment.ShowFriendRequest;


public class PicCitiFriendsPager extends FragmentStatePagerAdapter {
    public Fragment fragment;
    int tabCount;

    public PicCitiFriendsPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = null;

        switch (position) {
            case 0:
                fragment = new ShowAllUser();
                break;
            case 1:
                fragment = new ShowFriendList();
                break;
            case 2:
                fragment = new ShowFriendRequest();
                break;
            default:
                return null;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}