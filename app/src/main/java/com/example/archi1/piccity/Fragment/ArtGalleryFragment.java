package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.archi1.piccity.Adapter.ArtGalleryAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 11/25/2016.
 */

public class ArtGalleryFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public Utils utils;
    public Bitmap bitmap;
    public ArrayList<ArtGallery> artGalleryArrayList;
    public ArtGalleryAdapter artGalleryAdapter;

    public ArrayList<String> categoryIdArray;
    public ArrayList<String> categoryNameArry;


    public GridView artPhotoGridView;
    public String userId;
    public TextView textView;
    public String strSelectedImage;
    public Spinner spnerCategory;

    public ImageView dialogCaptureImage;
    public String strPrice, strLctn, strDesc;
    public LinearLayout fragmentLinearlayout;
    public String selectedCategoryList;
    public ImageView ivSearch;
    public ArrayList<ArtGallery> filterCategoryNameArray;
    String uNameStr, uPwdStr;
    public ArrayList<String> arrayUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_art_gallery, container, false);
        init(view);
        utils = new Utils(getActivity());
        ((Activity) getActivity()).setTitle(R.string.art_list);

        userId = utils.ReadSharePrefrence(getActivity(), Constant.UserId);
        artPhotoGridView = (GridView) view.findViewById(R.id.fragment_art_gallery_grid);
        fragmentLinearlayout = (LinearLayout) view.findViewById(R.id.fragment_art_gallery_ll_camera);
        spnerCategory = (Spinner) view.findViewById(R.id.spnrCategoryArtGallery);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearchArtGallery);
        ivSearch.setOnClickListener(this);
        categoryIdArray = new ArrayList<>();
        categoryNameArry = new ArrayList<>();
        filterCategoryNameArray = new ArrayList<>();
        fragmentLinearlayout.setOnClickListener(this);
        arrayUserName = new ArrayList<>();
        return view;
    }

    private void init(View view) {
        new getUploadedImage().execute();
        new getCategory().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.art_list);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_art_gallery_ll_camera:
                Log.d("gopu", "click");

                Bundle bundle = new Bundle();
                bundle.putString("Royalty", "isCameara");
                Fragment fragment = new SaleStuffFragment();
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;

            case R.id.ivSearchArtGallery:

                if (filterCategoryNameArray != null) {
                    filterCategoryNameArray.clear();
                }

                if (selectedCategoryList.equalsIgnoreCase("all")) {

                    artGalleryAdapter = new ArtGalleryAdapter(getActivity(), artGalleryArrayList);
                    artPhotoGridView.setAdapter(artGalleryAdapter);
                    artGalleryAdapter.notifyDataSetChanged();
                    Log.d("msg", "filter if" + artGalleryArrayList.size());

                } else {

                    Log.d("msg", "filter else " + filterCategoryNameArray.toString());

                    for (int i = 0; i < artGalleryArrayList.size(); i++) {
                        String name = artGalleryArrayList.get(i).getCategory();
                        if (selectedCategoryList.equalsIgnoreCase(name)) {
                            filterCategoryNameArray.add(artGalleryArrayList.get(i));
                        }
                    }

                    artGalleryAdapter = new ArtGalleryAdapter(getActivity(), filterCategoryNameArray);
                    artPhotoGridView.setAdapter(artGalleryAdapter);
                    artGalleryAdapter.notifyDataSetChanged();
                }
                Log.d("msg", "filter " + filterCategoryNameArray.toString());
                break;
        }
    }


    public class getCategory extends AsyncTask<String, String, String> {
        public ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlGetCategory = Constant.Base_URL + "get_category_list.php";
            return utils.MakeServiceCall(urlGetCategory);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {

                categoryIdArray.add("5");
                categoryNameArry.add("all");

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("true")) {
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayData.length(); i++) {

                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                        String categoryid = jsonObjectData.getString("category_id");
                        String categoryName = jsonObjectData.getString("category_name");

                        categoryIdArray.add(categoryid);
                        categoryNameArry.add(categoryName);


                    }


                    ArrayAdapter<String> spnerCategoryadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_view, categoryNameArry);
                    spnerCategory.setAdapter(spnerCategoryadapter);

                    spnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategoryList = categoryNameArry.get(position);
                            Log.d("msg", "selected category " + selectedCategoryList);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    private class getUploadedImage extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            artGalleryArrayList = new ArrayList<>();
        }


        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_upload_image.php?user_id=25*/

            /*http://web-medico.com/web1/pic_citi/Api/get_alluser_upload.php*/
            String Url = Constant.Base_URL + "get_alluser_upload.php";
            Log.d("UploadUrl", Url);

            return utils.getResponseofGet(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* //.dismiss();*/
            Log.d("msg", "ArtGAllery" + s);
            try {

                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        ArtGallery artGallery = new ArtGallery();
                        artGallery.setId(object.getString("id"));
                        artGallery.setImage(object.getString("image"));
                        artGallery.setPrice(object.getString("price"));
                        artGallery.setDescription(object.getString("description"));
                        //  artGallery.setLocation(object.getString("location"));
                        artGallery.setUsername(object.getString("user_name"));
                        artGallery.setName(object.getString("name"));
                        artGallery.setUserId(object.getString("user_id"));
                        artGallery.setCategory(object.getString("category_name"));
                        artGallery.setCurrency(object.getString("currency"));
                        Log.d("Image", object.getString("image"));
                        artGalleryArrayList.add(artGallery);
                    }

                    Log.d("artGAllery", artGalleryArrayList.toString());
                    artGalleryAdapter = new ArtGalleryAdapter(getActivity(), artGalleryArrayList);
                    artPhotoGridView.setAdapter(artGalleryAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
