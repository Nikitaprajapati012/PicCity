package com.example.archi1.piccity.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.ArtGalleryDetailsActivity;
import com.example.archi1.piccity.Activity.RoyaltyPicDetailsActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.Model.RoyaltyPicGallery;
import com.example.archi1.piccity.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by archi1 on 11/27/2016.
 */

public class RoyaltyPicAdapter extends BaseAdapter {
    public Utils utils;
    public String uid;
    public String pos;
    public int positionarray;
    public Context context;
    public ArrayList<RoyaltyPicGallery> royaltyPicGalleryArrayList;
    public LayoutInflater inflater;



    public RoyaltyPicAdapter(Context context, ArrayList<RoyaltyPicGallery> royaltyPicGalleryArrayList){
        this.context = context;
        this.royaltyPicGalleryArrayList = royaltyPicGalleryArrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return royaltyPicGalleryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.adapter_royalty_pic,null);
        utils = new Utils(context);
        String UserID = utils.ReadSharePrefrence(context, Constant.UserId);
        String UserNAme = utils.ReadSharePrefrence(context,Constant.Name);
        ImageView royaltyImage = (ImageView)convertView.findViewById(R.id.adapter_grid_item_royaltyimage);
        TextView royaltyOwner = (TextView)convertView.findViewById(R.id.adapter_grid_item_royalty_owner);
        final ImageView royaltyEditImage = (ImageView)convertView.findViewById(R.id.editImageView);
         royaltyEditImage.setVisibility(View.GONE);


         pos = royaltyPicGalleryArrayList.get(position).getId();
         final String strImage = royaltyPicGalleryArrayList.get(position).getImage();
         uid = royaltyPicGalleryArrayList.get(position).getUserid();


        Log.d("msg","royality img adaper path "+strImage);

        royaltyOwner.setText(royaltyPicGalleryArrayList.get(position).getName());

        if(!royaltyPicGalleryArrayList.get(position).getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(royaltyPicGalleryArrayList.get(position).getImage()).placeholder(R.drawable.ic_placeholder).into(royaltyImage);
        }

        if (UserID.equalsIgnoreCase(royaltyPicGalleryArrayList.get(position).getUserid())){
            royaltyEditImage.setVisibility(View.VISIBLE);
            royaltyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Alert Dialog");
                    alert.setMessage("Are You Sure You want To Delete ?");
                    alert.setIcon(R.drawable.ic_delete);
                    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            positionarray=position;
                            new deleteRoyaltyImage().execute();

                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                }
            });

        }else {
            royaltyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, RoyaltyPicDetailsActivity.class);
                    i.putExtra("id",pos);
                    i.putExtra("user_id",royaltyPicGalleryArrayList.get(position).getUserid());
                    i.putExtra("user_name",royaltyPicGalleryArrayList.get(position).getName());
                    i.putExtra("user_image",royaltyPicGalleryArrayList.get(position).getUser_profile());
                    i.putExtra("image",strImage);

                    context.startActivity(i);
                }
            });
        }

        return convertView;
    }

    private class deleteRoyaltyImage extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Url = Constant.Base_URL +"remove_royalty_pic.php?user_id="+uid +"&royalty_pic_id="+pos;
            Log.d("msg","Delete Url"+Url);

            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                if (object.getString("successful").equalsIgnoreCase("true")){
                   royaltyPicGalleryArrayList.remove(positionarray);
                    notifyDataSetChanged();
                    Toast.makeText(context, ""+object.getString("msg"), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, ""+object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();

        }
    }
}
