package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.archi1.piccity.Adapter.AListAdapter;
import com.example.archi1.piccity.Adapter.RoyaltyPicAdapter;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

import java.util.ArrayList;

/**
 * Created by archi1 on 12/23/2016.
 */

public class AList extends Fragment implements View.OnClickListener{

    public Utils utils;
    public Context context;
    public GridView aListGridView;
    public TextView txtview;
    public ImageView imgview;
    public AListAdapter adapter;
    public RelativeLayout layout;
    public LinearLayout aListPicLoad;
    private ArrayList<String> stringArrayList , searchArrayList;
    public EditText searchAlistimageEdt;
    public AListAdapter alistPicAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_a_list, container, false);
        init(view);
        setHasOptionsMenu(true);
        imgview = (ImageView) view.findViewById(R.id.fragment_royalty_gallery_iv_camera);
        setData();
        aListGridView = (GridView) view.findViewById(R.id.fragment_a_list_gridview);
        aListGridView.setAdapter(new AListAdapter(getActivity(),stringArrayList));

        searchAlistimageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchArrayList = new ArrayList<String>();
                for (int i=0;i<stringArrayList.size();i++)
                {
                    if (stringArrayList.get(i).toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        searchArrayList.add(stringArrayList.get(i));
                    }
                }
                alistPicAdapter = new AListAdapter(getContext(), searchArrayList);
                aListGridView.setAdapter(alistPicAdapter);

            }
        });
        return view;
    }

    private void init(View view) {
        searchAlistimageEdt = (EditText)view.findViewById(R.id.fragment_alist_search);
        aListPicLoad = (LinearLayout) view.findViewById(R.id.fragment_royalty_gallery_ll_camera_alist);
        aListPicLoad.setOnClickListener(this);


    }
        private void setData() {
            stringArrayList = new ArrayList<>();
            stringArrayList.add("Quynh Trang");
            stringArrayList.add("Hoang Bien");
            stringArrayList.add("Duc Tuan");
            stringArrayList.add("Dang Thanh");
            stringArrayList.add("Xuan Luu");
            stringArrayList.add("Phan Thanh");
            stringArrayList.add("Kim Kien");
            stringArrayList.add("Ngo Trang");
            stringArrayList.add("Thanh Ngan");
            stringArrayList.add("Nguyen Duong");
            stringArrayList.add("Quoc Cuong");
            stringArrayList.add("Tran Ha");
            stringArrayList.add("Vu Danh");
            stringArrayList.add("Minh Meo");

        }

    @Override
    public void onResume() {
        //getActivity().setTitle(R.string.alist);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_royalty_gallery_ll_camera_alist:
                Fragment fragment = new SaleStuffFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Royalty", "RoyaltyPicFragment");
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }
    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    aListGridView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });


    }*/
}
