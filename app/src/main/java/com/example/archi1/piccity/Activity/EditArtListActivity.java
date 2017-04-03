package com.example.archi1.piccity.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by archirayan on 6/12/16.
 */

public class EditArtListActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edtDes, edtPrice;
    Spinner spnerPrice;
    ImageView ivProduct;
    Button btnUpdate, btnDelete;
    ArrayList<String> arrayCurreType;
    String imgID;
    String imgDescription;
    String imgPath;
    String imgPric;
    String name;
    String imgLocation;
    String username;
    String userid;
    ImageView ivEditImage;
    EditText edtName;
    private int SELECT_IMAGE = 100;
    private Bitmap rotatedBitmap;
    private Fragment fragment;
    private String USERID;

    public static Bitmap RotateBitmap(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_artlist_images);


        arrayCurreType = new ArrayList<>();
        arrayCurreType.add("INR");
        arrayCurreType.add("USD");
        arrayCurreType.add("EUR");


        edtDes = (EditText) findViewById(R.id.edtDEscription);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        spnerPrice = (Spinner) findViewById(R.id.spnerPrice);
        ivProduct = (ImageView) findViewById(R.id.ivProduct);
        ivProduct.setScaleType(ImageView.ScaleType.FIT_XY);
        ivEditImage = (ImageView) findViewById(R.id.ivEditImage);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        ivEditImage.setOnClickListener(this);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(EditArtListActivity.this, android.R.layout.simple_spinner_item, arrayCurreType);
        spnerPrice.setAdapter(stringArrayAdapter);


        if (getIntent().getExtras() != null) {

            imgID = getIntent().getExtras().getString("id");
            imgDescription = getIntent().getExtras().getString("description");
            imgPath = getIntent().getExtras().getString("image");
            imgPric = getIntent().getExtras().getString("price");
            name = getIntent().getExtras().getString("name");
            imgLocation = getIntent().getExtras().getString("location");
            username = getIntent().getExtras().getString("username");
            userid = getIntent().getExtras().getString("userid");

            USERID = Utils.ReadSharePrefrence(EditArtListActivity.this, Constant.UserId);
            Log.d("msg", "vijay Image Pah" + imgPath);

        }


        edtDes.setText(imgDescription);
        edtName.setText(name);
        edtPrice.setText(imgPric);


        spnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        new RotateBitmapAsy().execute();


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ivEditImage:

                openGallry();

                break;

            case R.id.btnUpdate:

                UpdateArtProduct();


                break;
            case R.id.btnDelete:

                DeleteProductAsy();

                break;
        }
    }

    public void UpdateArtProduct() {


/*
        http://web-medico.com/web1/pic_citi/Api/update_user_upload.php?id=114&user_id=25&image=1.jpg&name=bb&price=5000
        &description=xyz&location=abc*/


        Log.d("msg", "vijay Image Pah  2  " + imgPath);


        Log.d("msg", "img id " + imgID + " user id " + USERID);

        Ion.with(EditArtListActivity.this)
                .load(Constant.Base_URL + "update_user_upload.php?")
                .setMultipartParameter("id", imgID)
                .setMultipartParameter("user_id", USERID)
                .setMultipartFile("image", new File(imgPath))
                .setMultipartParameter("name", edtName.getText().toString())
                .setMultipartParameter("description", edtDes.getText().toString())
                .setMultipartParameter("location", imgLocation)
                .setMultipartParameter("price", edtPrice.getText().toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {


                        Log.d("msg", "result11  " + result);
                        Intent intentHome = new Intent(EditArtListActivity.this, MainActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentHome);
                        finish();

                    }
                });


    }


    public void openGallry() {

            /*   Intent intent = new Intent();
            intent.setType("image*//*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
*/


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        }

    }


    @TargetApi(19)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
               if (requestCode == SELECT_IMAGE) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            try {
                                Uri uri = data.getData();

                          imgPath=     getPath(EditArtListActivity.this,uri);

                                Log.d("msg","ip  "+imgPath);
                              ivProduct.setImageURI(uri);

                                Log.d("msg","vijay Image Pah 3  "+imgPath);
                                Log.d("msg", "edtit acitivyt img path " + imgPath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(EditArtListActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }




    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }



    public String getPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(proj[0]);
            res = cursor.getString(columnIndex);
        }

        cursor.close();
        return res;


    }


    public void DeleteProductAsy() {
/*
        http://web-medico.com/web1/pic_citi/Api/delete_user_upload.php?id=2&user_id=25
*/
        Log.d("msg", "img id   " + imgID + "    user id " + Utils.ReadSharePrefrence(EditArtListActivity.this, Constant.UserId));
        Ion.with(getApplicationContext())
                .load(Constant.Base_URL + "delete_user_upload.php?")
                .setMultipartParameter("id", imgID)
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(EditArtListActivity.this, Constant.UserId))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        Log.d("msg", "delete as res  " + result);


                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("true")) {
                                Toast.makeText(EditArtListActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditArtListActivity.this, MainActivity.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(EditArtListActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Intent intentHome = new Intent(EditArtListActivity.this, MainActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentHome);
                        finish();

                    }
                });

    }

    class RotateBitmapAsy extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                Log.d("msg", "detial activity img path " + imgPath);
                URL url = new URL(imgPath);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                rotatedBitmap = RotateBitmap(image, 90f);
                Log.d("msg", "rotatedBitmap   " + rotatedBitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ivProduct.setImageBitmap(rotatedBitmap);
        }
    }
}




