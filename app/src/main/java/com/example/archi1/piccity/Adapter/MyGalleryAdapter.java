package com.example.archi1.piccity.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.PopupMenu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.Canvas_MyGallery_Pics;
import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Activity.MainActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.Model.MyGalleryModel;
import com.example.archi1.piccity.R;
import com.google.android.gms.vision.text.Text;
import com.paypal.android.sdk.m;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.archi1.piccity.Activity.ArtGalleryDetailsActivity.RotateBitmap;
import static com.example.archi1.piccity.R.id.down;
import static com.example.archi1.piccity.R.id.imageView;

/**
 * Created by archi on 30-Mar-17.
 */

public class MyGalleryAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<MyGalleryModel> galleryDetailsList;
    public LayoutInflater inflater;
    protected Utils utils;
    private ImageView ivOption;
    private TextView tvImageName;
    private Dialog dialog;
    private String strImage, strImageId, strImageName;
    private Bitmap rotatedBitmap;


    @Override
    public int getCount() {
        return galleryDetailsList.size();
    }

    public MyGalleryAdapter(Context mContext, List<MyGalleryModel> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = (ArrayList<MyGalleryModel>) galleryDetailsList;
        this.utils = new Utils(mContext);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        final View temp;
        MyGalleryModel mygallerymodel = galleryDetailsList.get(position);
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.adapter_mygallery, parent, false);
            temp = convertView;
            ImageView ivSetImage = (ImageView) convertView.findViewById(R.id.adapter_mygallery_image);
            ivOption = (ImageView) convertView.findViewById(R.id.ivOption);
            tvImageName = (TextView) convertView.findViewById(R.id.tvImageName);
            ivOption.setTag(position);

            MyGalleryModel album = galleryDetailsList.get(position);
            strImage = galleryDetailsList.get(position).getImg();
            strImageId = galleryDetailsList.get(position).getId();
            strImageName = galleryDetailsList.get(position).getName();

            Log.d("img", strImage);
            Picasso.with(mContext).load(mygallerymodel.getImg()).placeholder(R.drawable.ic_placeholder).into(ivSetImage);
            ivOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("POS@", "" + position);
                    showpopupmenu((ImageView) temp.findViewWithTag(position));
                }
            });
        }
        return convertView;
    }

    private void showpopupmenu(ImageView ivOption) {
        PopupMenu popupMenu = new PopupMenu(mContext, ivOption);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_mygallery, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }

    private class MyMenuItemClickListener implements OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.adapter_msg:

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);

                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "");
                    smsIntent.putExtra("sms_body", "");

                    try {
                        mContext.startActivity(smsIntent);
                        Log.i("Finished sending SMS...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Log.d("SMS", "" + ex);
                        Toast.makeText(mContext,
                                "SMS faild, please try again later." + ex, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext, "send my gallery", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.adapter_share_fb:

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image via");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello...." + " " + strImage);
                        Bundle facebookBundle = new Bundle();
                        facebookBundle.putString(Intent.EXTRA_TEXT, strImage);
                        Bundle replacement = new Bundle();
                        replacement.putBundle("com.facebook.katana", facebookBundle);
                        chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
                    } else {
                        shareIntent.putExtra(Intent.EXTRA_TEXT, strImage);
                    }

                    chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(chooserIntent);
                    break;


                case R.id.canvas_image:

                    Intent i = new Intent(mContext, Canvas_MyGallery_Pics.class);
                    i.putExtra("Id", strImageId);
                    i.putExtra("Image", strImage);
                    i.putExtra("ImageName", strImageName);

                    mContext.startActivity(i);
                default:
            }
            return false;
        }
    }
}