package com.example.archi1.piccity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

/**
 * Created by archi on 31-Mar-17.
 */

public class PersonalArtistFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;
    public Context mContext;
    //This is our viewPager
    private ViewPager viewPager;
    private Utils utils;
    private Pager adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    // @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_pesonal_artist, container, false);

        //Adding onTabSelectedListener to swipe views
       // tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) getActivity().getSupportFragmentManager());
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils = new Utils(getActivity());
        mContext = getActivity();

        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        //Creating our pager adapter
        adapter = new Pager(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
      //  tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) getActivity().getSupportFragmentManager());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);

    }

    // @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    // @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    //  @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}