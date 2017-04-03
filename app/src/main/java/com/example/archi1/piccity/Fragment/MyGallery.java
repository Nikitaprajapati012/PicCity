package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.MyGalleryAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.MyGalleryModel;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archi on 20-Mar-17.
 */

public class MyGallery extends android.support.v4.app.Fragment {

    public ArrayList<MyGalleryModel> galleryArray;
    private MyGalleryAdapter mAdapter;
   // public RecyclerView recyclerView;
    public MyGalleryModel myGalleryModel;
    public ListView lvImageGallery;
    //public ImageView pagerImageView;
    public Utils utils;
    public CollapsingToolbarLayout collapsingToolbar;
    public AppBarLayout appBarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_mygallery, container, false);
        utils = new Utils(getActivity());

        lvImageGallery = (ListView) view.findViewById(R.id.lvImageGallery);
       // new getGallery().execute();

        if (utils.isConnectingToInternet() == true) {

            new getGallery().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    @Override
    public void onResume() {
        // getActivity().setTitle(R.string.art_list);
        //((Activity) getActivity()).setTitle(R.string.gallery);
        super.onResume();

    }


    private void initCollapsingToolbar() {
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();

                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });


    }

    private class getGallery extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String user_id_gallery;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_id_gallery = Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_image_data.php*/
            String Url = Constant.Base_URL + "get_image_by_user_id.php?user_id=" + user_id_gallery;
            Log.d("URl",""+Url);
            return utils.MakeServiceCall(Url);/*http://web-medico.com/web1/pic_citi/Api/get_image_size.php*/

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("log..", s);
            try {
                galleryArray = new ArrayList<>();
                JSONObject mainobject = new JSONObject(s);

                if (mainobject.getString("status").equalsIgnoreCase("true")) {

                    JSONArray jsonArray = mainobject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        myGalleryModel = new MyGalleryModel();
                        myGalleryModel.setId(jsonObject.getString("id"));
                       // gallery.setName(jsonObject.getString("name"));
                        myGalleryModel.setImg(jsonObject.getString("img"));
                        //gallery.setPrice(jsonObject.getString("imgprice"));
                        galleryArray.add(myGalleryModel);

                        Log.d("ARRAY",""+galleryArray);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new MyGalleryAdapter(getActivity(), galleryArray);
            lvImageGallery.setAdapter(mAdapter);
        }

        public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

            private int spanCount;
            private int spacing;
            private boolean includeEdge;


            GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
                this.spanCount = spanCount;
                this.spacing = spacing;
                this.includeEdge = includeEdge;

            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                int position = parent.getChildAdapterPosition(view);
                int column = position % spanCount;
                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }
                }
            }
        }

        private int dpToPx(int dp) {
            Resources r = getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

        }
    }
}