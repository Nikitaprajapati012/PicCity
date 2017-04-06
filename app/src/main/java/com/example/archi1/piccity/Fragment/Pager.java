package com.example.archi1.piccity.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.archi1.piccity.OneFragment;
import com.example.archi1.piccity.SecondFragment;

/**
 * Created by archi on 03-Apr-17.
 */
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;


    public Pager(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        //Initializing tab count
        this.tabCount= tabCount;
    }


    //Overriding method getItem
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                OneFragment tab1 = new OneFragment();
                return tab1;
            case 1:
                SecondFragment tab2 = new SecondFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}