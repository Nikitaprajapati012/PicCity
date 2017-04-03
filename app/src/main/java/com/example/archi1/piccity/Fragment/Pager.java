package com.example.archi1.piccity.Fragment;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.archi1.piccity.OneFragment;
import com.example.archi1.piccity.TwoFragment;

/**
 * Created by archi on 03-Apr-17.
 */
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
   /* public Pager(FragmentManager fm, int tabCount) {
        super(fm);

        //Initializing tab count
        this.tabCount= tabCount;
    }*/

    /*public Pager(FragmentActivity fm, int tabCount) {

        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }
*/
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
                TwoFragment tab2 = new TwoFragment();
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