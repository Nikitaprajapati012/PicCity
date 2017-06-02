package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.PersoinalArtistProfilePager;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utility;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.PersonalArtistPage;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/** * Created by Ravi archi on 1/10/2017.
 */

public class PersonalArtistPageFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    public String userChoosenTask, ImagePath,userId, artistId, artistContactNumber,artistProfilePic;
    public Bitmap bitmap;
      public Utils utils;
    public Uri uri, videoUri;
    @BindView(R.id.fragment_personal_artist_page_imgprofilepic)
    ImageView imgProfilePic;
    @BindView(R.id.fragment_personal_artist_page_txtuploadoptionphotos)
    TextView txtUploadOptionPhotos;
    @BindView(R.id.fragment_personal_artist_page_txtuploadoptionvideo)
    TextView txtUploadOptionVideos;
    @BindView(R.id.fragment_personal_artist_page_txtusername)
    TextView txtUserName;
    @BindView(R.id.fragment_personal_artist_page_uploadoption)
    LinearLayout layoutUploadOption;
    TabLayout tabLayout;
    ViewPager viewPager;
    public int MEDIA_TYPE_VIDEO = 22, REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_VIDEO_FILE = 2, REQUEST_CAMERA_VIDEO = 3;
    public Dialog dialog;
    public Button btnMessage, btnCancel;
    public ImageView imgArtistProfilePic;
    public ArrayList<PersonalArtistPage> arrayList;
    public PersonalArtistPage details;
    public CircleImageView userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_personal_artist_page, container, false);
        ButterKnife.bind(this, view);
        //initial  pic
        init();
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Photo"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // TODO: 3/22/2017 set view pager
        PersoinalArtistProfilePager adapter = new PersoinalArtistProfilePager(getActivity(),details,getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        // TODO: 3/22/2017  Adding adapter to pager
        viewPager.setAdapter(adapter);
        // TODO: 3/22/2017  Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        click();
        return view;
    }

    // TODO: 4/14/2017 intialization
    private void init() {
        //userProfile = (CircleImageView)getActivity().findViewById(R.id.user_profile);
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.USER_ID);
        if(getArguments() != null){
            details = new PersonalArtistPage();
            Gson gson=new Gson();
            String strDetails=getArguments().getString("friendlistdetails");
            details= gson.fromJson(strDetails,PersonalArtistPage.class);
            txtUserName.setText(details.getUserName());
            artistProfilePic=details.getUserImage();
            Picasso.with(getActivity()).load(artistProfilePic).placeholder(R.drawable.ic_placeholder).into(imgProfilePic);
            artistId = details.getUserId();
            artistContactNumber = details.getUserContactNumber();
        }
        if (userId.equalsIgnoreCase(artistId)){
            layoutUploadOption.setVisibility(View.VISIBLE);
            imgProfilePic.setClickable(false);
        }
        else{
            layoutUploadOption.setVisibility(View.INVISIBLE);
            imgProfilePic.setClickable(true);
        }
    }

    // TODO: 4/13/2017 perform click
    private void click() {
        txtUploadOptionPhotos.setOnClickListener(this);
        txtUploadOptionVideos.setOnClickListener(this);
        txtUserName.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_personal_artist_page_txtuploadoptionphotos:
                openDialogPhotos();
                break;
            case R.id.fragment_personal_artist_page_txtuploadoptionvideo:
                openDialogVideos();
                break;
            case R.id.fragment_personal_artist_page_txtusername:
                break;
            case R.id.fragment_personal_artist_page_imgprofilepic:
                openDialogUserProfilePic(details);
                break;
        }
    }

    // TODO: 4/29/2017 zoom  view of the user profile pic and details
    private void openDialogUserProfilePic(PersonalArtistPage details) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_userinfo);
        dialog.setTitle("");
        btnMessage = (Button) dialog.findViewById(R.id.dialog_userchat);
        btnCancel = (Button) dialog.findViewById(R.id.dialog_usercancel);
        imgArtistProfilePic = (ImageView) dialog.findViewById(R.id.dialog_userimage);
        Picasso.with(getActivity()).load(details.getUserImage()).placeholder(R.drawable.ic_placeholder).into(imgArtistProfilePic);
        dialog.show();
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent ilogout = new Intent(context, .class);
                ilogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(ilogout);*/
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // TODO: 4/22/2017 store the image & video as file
    private void storeImageVideo() {
        Log.d("path@", "" + ImagePath + "----Id------" + artistId);
        //http://web-medico.com/web1/pic_citi/Api/personal_artist.php?user_id=25&file=jarry.jpg
        Ion.with(getContext())
                .load(Constant.Base_URL + "personal_artist.php?")
                .setMultipartParameter("user_id", artistId)
                .setMultipartFile("file", new File(ImagePath))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("JSONRESULT_image@video@" + "", result);
                    }
                });
    }

    private void openDialogVideos() {
        final CharSequence[] items = {/*"Take Video", */"Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
               /* if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result)
                        cameraIntentVideos();
                } else*/ if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntentVideos();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntentVideos() {
        Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickImage, SELECT_VIDEO_FILE);
    }

    // TODO: 4/13/2017 choose from camera for video
    private void cameraIntentVideos() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "myvideo.mp4";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, fileName);
            videoUri = getActivity().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(takePictureIntent,REQUEST_CAMERA_VIDEO);
        }
        /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CAMERA_VIDEO);*/
    }

    private Uri getOutputMediaFileUri(int media_type_video) {
        return Uri.fromFile(getOutputMediaFile(media_type_video));
    }

    private File getOutputMediaFile(int media_type_video) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getActivity(), "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());
        File mediaFile;
        if (media_type_video == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void openDialogPhotos() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // TODO: 4/13/2017 choose from gallery for image
    private void galleryIntent() {
        Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentPickImage, SELECT_FILE);
    }

    // TODO: 4/13/2017 choose from camera for image
    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_CAMERA_VIDEO) {
                onCaptureVideoResult(data);
            } else if (requestCode == SELECT_VIDEO_FILE) {
                onSelectVideoFromGalleryResult(data);
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onSelectVideoFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                if (cursor == null || cursor.getCount() < 1) {
                    return; // no cursor or no record. DO YOUR ERROR HANDLING
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                ImagePath = cursor.getString(columnIndex);
                Log.d("video@@", ImagePath);
                // TODO: 4/24/2017 store the video path as file
                storeImageVideo();
                cursor.close(); // close cursor
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCaptureImageResult(Intent data) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null,
                null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        ImagePath = cursor.getString(column_index_data);
        // TODO: 4/24/2017 store the image path as file
        storeImageVideo();
        Log.d("imagepath@@", "@@" + ImagePath);
    }

    public void onCaptureVideoResult(Intent data) {
        Log.d("Video File", "@@" + data.getData());
        videoUri = data.getData();
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().managedQuery(videoUri, projection, null, null, null);
        Log.d("cursor", "@@" + cursor);
        int column_index_data = cursor.getColumnIndexOrThrow(
                MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        ImagePath = cursor.getString(column_index_data);
        Log.d("videopath@@", "@@" + ImagePath);
        // TODO: 4/24/2017 store the image path as file
        storeImageVideo();
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                if (cursor == null || cursor.getCount() < 1) {
                    return; // no cursor or no record. DO YOUR ERROR HANDLING
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                ImagePath = cursor.getString(columnIndex);
                Log.d("image@@", ImagePath);
                // TODO: 4/24/2017 store the image path as file
                storeImageVideo();
                cursor.close(); // close cursor
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }
}
