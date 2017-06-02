package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.archi1.piccity.Adapter.RoyaltyPicAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.RoyaltyPicGallery;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 11/27/2016.
 */

public class RoyaltyPicFragment extends Fragment implements View.OnClickListener {
    public ArrayList<RoyaltyPicGallery> royaltyPicGalleryArrayList, searchedArraylist;
    public RoyaltyPicAdapter royaltyPicAdapter;
    public Utils utils;
    public GridView royaltyPhotoGridView;
    public EditText searchRoyaltyImageEdt;
    public String userId;
    public LinearLayout royaltyPicUploadlayout;
    private RoyaltyPicGallery details;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_royalty_pic, container, false);
        init(view);
        utils = new Utils(getActivity());
        userId = utils.ReadSharePrefrence(getActivity(), Constant.UserId);

        // ((Activity) getActivity()).setTitle(R.string.royalty);

        new getRoyalityImages().execute();


        searchRoyaltyImageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchedArraylist = new ArrayList<>();
                for (int i = 0; i < royaltyPicGalleryArrayList.size(); i++) {
                    if (royaltyPicGalleryArrayList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchedArraylist.add(royaltyPicGalleryArrayList.get(i));
                    }
                }

                royaltyPicAdapter = new RoyaltyPicAdapter(getActivity(), searchedArraylist);
                royaltyPhotoGridView.setAdapter(royaltyPicAdapter);
            }
        });
        return view;
    }

    private void init(View view) {
        searchRoyaltyImageEdt = (EditText) view.findViewById(R.id.fragment_royalty_search);
        royaltyPhotoGridView = (GridView) view.findViewById(R.id.fragment_royalty_gridview);
        royaltyPicUploadlayout = (LinearLayout) view.findViewById(R.id.fragment_royalty_gallery_ll_camera);
        royaltyPicUploadlayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.royalty);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_royalty_gallery_ll_camera:
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


    private class getRoyalityImages extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
            royaltyPicGalleryArrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web1/pic_citi/Api/get_royalty_pic_list.php?user_id=25
            String Url = Constant.Base_URL + "get_royalty_pic_list.php";
            Log.d("royality url", Url);
            Log.d("Username", userId);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        details = new RoyaltyPicGallery();
                        details.setId(object.getString("id"));
                        details.setImage(object.getString("watermarkimage"));
                        details.setUserid(object.getString("user_id"));
                        details.setName(object.getString("name"));
                        details.setPaypal_email(object.getString("paypal_email"));
                        details.setUser_profile(object.getString("profile_pic"));
                        royaltyPicGalleryArrayList.add(details);
                        Log.d("image", (object.getString("paypal_email")));
                    }
                    royaltyPicAdapter = new RoyaltyPicAdapter(getActivity(), royaltyPicGalleryArrayList);
                    royaltyPhotoGridView.setAdapter(royaltyPicAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
}
