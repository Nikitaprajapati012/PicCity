package com.example.archi1.piccity.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Fragment.AList;
import com.example.archi1.piccity.Fragment.ArtGalleryFragment;
import com.example.archi1.piccity.Fragment.GalleryFragment;
import com.example.archi1.piccity.Fragment.MyGallery;
import com.example.archi1.piccity.Fragment.PersonalArtistFragment;
import com.example.archi1.piccity.Fragment.RoyaltyPicFragment;
import com.example.archi1.piccity.Fragment.SaleStuffFragment;
import com.example.archi1.piccity.R;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;

import static android.R.id.toggle;
import static com.example.archi1.piccity.R.id.text;
import static com.example.archi1.piccity.R.id.view;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    public String strImgProfile = "";
    public Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getApplicationContext().getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);


        TextView textView = (TextView) findViewById(R.id.nav_header_username);
        textView.setText(Utils.ReadSharePrefrence(MainActivity.this, Constant.Name));
        ImageView imageView = (ImageView) findViewById(R.id.nav_header_image);
        //strImgProfile = Utils.ReadSharePrefrence(MainActivity.this,Constant.Image);
        Glide.with(MainActivity.this).load(Utils.ReadSharePrefrence(MainActivity.this, Constant.Image)).placeholder(R.drawable.ic_profile).into(imageView);

        /* if (strImgProfile.equalsIgnoreCase(""))
       {

           Glide.with(MainActivity.this).load().placeholder(R.drawable.ic_profile).into(imageView);
       }
       else
       {*/
        // Glide.with(MainActivity.this).load(strImgProfile).placeholder(R.drawable.ic_profile).into(imageView);


        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        displayView(0);


        if (checkPermissionForCamera()) {

        } else {

            requestPermissionForCamera();
        }


        if (checkPermissionForExternalStorage()) {

        } else {

            requestPermissionForExternalStorage();

        }

        if (checkPermissionForReadExternalStorage()) {

        } else {

            requestPermissionForReadExternalStorage();

        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);

    }

    private void displayView(int position) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new GalleryFragment();
                title = getString(R.string.gallery);
                Toast.makeText(getApplicationContext(), "gallery", Toast.LENGTH_SHORT).show();
                break;


            case 1:
                fragment = new SaleStuffFragment();
                title = getString(R.string.camera);
                Toast.makeText(getApplicationContext(), "Sale Your Art", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                fragment = new MyGallery();
                title = getString(R.string.mygallery);
                Toast.makeText(getApplicationContext(), "My Gallery", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                fragment = new ArtGalleryFragment();
                title = getString(R.string.alist);
                Toast.makeText(getApplicationContext(), "A List", Toast.LENGTH_SHORT).show();
                break;
/*
            case 3:
     *//*           fragment = new ArtGalleryFragment();
                title = getString(R.string.art_list);
                Toast.makeText(getApplicationContext(), "art_list", Toast.LENGTH_SHORT).show();
     *//*           break;*/

            case 4:
                fragment = new RoyaltyPicFragment();
                title = getString(R.string.royalty);
                Toast.makeText(getApplicationContext(), "royalty", Toast.LENGTH_SHORT).show();
                break;

            case 5:
                break;

            case 6:
                break;

            case 7:
                fragment = new PersonalArtistFragment();
                title = getString(R.string.personal_artist);
                break;

            case 8:
                title = getString(R.string.logout);
                Utils.clearSharedPreferenceData(MainActivity.this);
                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                break;
            case 9:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share image via...");
                String link = "www.playstore.com";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    shareIntent.putExtra(Intent.EXTRA_TEXT, text + " " + link);
                    Bundle facebookBundle = new Bundle();
                    facebookBundle.putString(Intent.EXTRA_TEXT, link);
                    Bundle replacement = new Bundle();
                    replacement.putBundle("com.facebook.katana", facebookBundle);
                    chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
                } else {
                    shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                }

                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
                default:
                    break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        Log.d("gopluu", "vijay " + uri);

        Intent intent = new Intent(MainActivity.this, CameraImagePriview.class);
        intent.putExtra("GalleryImagePath", uri.toString());
        intent.putExtra("status", "gallery");
        startActivity(intent);
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            int EXTERNAL_STORAGE_PERMISSION_READ_REQUEST_CODE = 111;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_READ_REQUEST_CODE);
        }
    }


    public void requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            int CAMERA_PERMISSION_REQUEST_CODE = 321;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}
