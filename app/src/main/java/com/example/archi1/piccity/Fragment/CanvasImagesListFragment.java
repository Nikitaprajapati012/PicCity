package com.example.archi1.piccity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import com.example.archi1.piccity.Adapter.CanvasImageListAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 4/22/2017.
*/

public class CanvasImagesListFragment extends Fragment {
    public RecyclerView recyclerViewCanvasImages;
    public ArrayList<GalleryDetails> canvasImageList;
    public CanvasImageListAdapter adapter;
    public Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_canvasimages, container, false);
        utils = new Utils(getActivity());
        recyclerViewCanvasImages=(RecyclerView)view.findViewById(R.id.fragment_canvasimages_recycler_canvasimageslist);
        if (utils.isConnectingToInternet()) {
            new getCanvasImageList().execute();
        }
        else
        {
            Toast.makeText(getContext(), "Please Check Internet Connectivity...", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.canvaspic);
    }

    private class getCanvasImageList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            canvasImageList = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /*http://web-medico.com/web1/pic_citi/Api/get_image_data.php*/
            String Url = Constant.Base_URL + "get_canvas_images.php";
            Log.d("URL_CHAT",""+ Url);
            return utils.getResponseofGet(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("CHATLOG.",s);
            try {
                JSONObject mainobject = new JSONObject(s);
                if (mainobject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = mainobject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GalleryDetails canvasimage = new GalleryDetails();
                        canvasimage.setId(jsonObject.getString("user_id"));
                        canvasimage.setCanvas_image(jsonObject.getString("canvasimage"));
                        canvasimage.setName(jsonObject.getString("name"));
                        canvasImageList.add(canvasimage);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new CanvasImageListAdapter(getActivity(), canvasImageList);
            recyclerViewCanvasImages.setAdapter(adapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCanvasImages.setLayoutManager(mLayoutManager);
            recyclerViewCanvasImages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCanvasImages.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
