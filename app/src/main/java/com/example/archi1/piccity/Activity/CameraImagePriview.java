package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Fragment.SaleStuffFragment;
import com.example.archi1.piccity.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class CameraImagePriview extends AppCompatActivity implements View.OnClickListener {


    public EditText dialogPrice, dialogDesc, dialogLoc, dialogTitle;
    public Button dialogDone, dialogCancel;
    public Spinner dialogSpnerPrice;
    public ArrayList<String> arrayListPrice;
    private String imagePath;
    private Spinner dialogCategory;
    private ArrayList<String> arrayCategoryList;
    private String selectedCategoryName;
    private ArrayList<String> categoryIdArray;
    private ArrayList<String> categoryNameArray;
    private String selectedPriceType;
    private String activityStatus;
    private FloatingActionButton btnRetakeImage, btnCanvas;
    private ImageView ivCapture;
    private FloatingActionButton btnImagePost;
    private FloatingActionButton btnFIlter, btnCanvasPic;
    private FloatingActionMenu materialDesignFAM;
    private int SELECT_IMAGE = 200;
    private int FILTER_IMAGE = 400;


    public static Bitmap RotateBitmap(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_screen);


        ivCapture = (ImageView) findViewById(R.id.captureimage);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);


        btnCanvas = (FloatingActionButton) findViewById(R.id.btn_Image_makeCanvas);
        btnImagePost = (FloatingActionButton) findViewById(R.id.btn_Image_Post);
        btnRetakeImage = (FloatingActionButton) findViewById(R.id.btn_retake_image);
        btnFIlter = (FloatingActionButton) findViewById(R.id.btn_filter);
        btnCanvasPic = (FloatingActionButton) findViewById(R.id.btn_canvastpic);




        /*materialDesignFAM.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened){
                    Toast.makeText(CameraImagePriview.this, "Menu is open", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CameraImagePriview.this, "Menu is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        btnFIlter.setOnClickListener(this);
        btnCanvasPic.setOnClickListener(this);


        arrayListPrice = new ArrayList<>();
        categoryIdArray = new ArrayList<>();
        categoryNameArray = new ArrayList<>();

        arrayListPrice.add("IND");
        arrayListPrice.add("USD");

        materialDesignFAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                }
            }
        });

        /*arrayCategoryList = new ArrayList<>();
        arrayCategoryList.add("Electronic");
        arrayCategoryList.add("Cars and Motors");
        arrayCategoryList.add("Sports Leisure and Games");
        arrayCategoryList.add("Home and Garden");
        arrayCategoryList.add("Movies ,Books and Music");
        arrayCategoryList.add("Fashion and Accessories");*/

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String status = bundle.getString("status");


            if (status.equalsIgnoreCase("gallery")) {

                String galleryImagePath = getIntent().getStringExtra("GalleryImagePath");
                /* Bitmap newBitmap = BitmapFactory.decodeFile(galleryImagePath);*/

                Log.d("msg", "gallery path " + galleryImagePath);

                imagePath = getPathFromURI(Uri.parse(galleryImagePath));
                Bitmap newBitmap = BitmapFactory.decodeFile(imagePath);
                ivCapture.setImageBitmap(newBitmap);
                Log.d("jaydip", imagePath);


            } else {

                if (bundle.containsKey("path")) {
                    imagePath = bundle.getString("path", "");
                    activityStatus = bundle.getString("ActivitStatus");
                    Log.d("rujul", "d  " + imagePath);
                    Log.d("rujul", "activityStatus " + activityStatus);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap newBitmap = RotateBitmap(myBitmap, 90f);
                    ivCapture.setImageBitmap(newBitmap);
                }
            }


        }

        if (activityStatus.equalsIgnoreCase("RoyaltyPicFragment")) {
            // hide buttons
            btnImagePost.setVisibility(View.GONE);
            btnCanvasPic.setVisibility(View.GONE);
            btnFIlter.setVisibility(View.GONE);
        } else if (activityStatus.equalsIgnoreCase("SaleStuff")) {

        }

        btnCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CameraImagePriview.this, "click ", Toast.LENGTH_SHORT).show();
                AddToRoyality();
                materialDesignFAM.close(true);

            }
        });


        btnImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                materialDesignFAM.close(true);
                final Dialog dialog = new Dialog(CameraImagePriview.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image_upload);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialogPrice = (EditText) dialog.findViewById(R.id.et_upload_img_price);
                dialogDesc = (EditText) dialog.findViewById(R.id.et_upload_img_description);
                dialogLoc = (EditText) dialog.findViewById(R.id.et_upload_img_location);
                dialogTitle = (EditText) dialog.findViewById(R.id.et_title);
                dialogDone = (Button) dialog.findViewById(R.id.btn_done);
                dialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);
                //dialogCategory = (Spinner) dialog.findViewById(R.id.et_upload_img_category);
                dialogSpnerPrice = (Spinner) dialog.findViewById(R.id.spnrPrice);
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(CameraImagePriview.this, R.layout.spinner_item_view, arrayListPrice);
                dialogSpnerPrice.setAdapter(stringArrayAdapter);


                arrayCategoryList = new ArrayList<>();
                arrayCategoryList.add("Electronic");
                arrayCategoryList.add("Cars and Motors");
                arrayCategoryList.add("Sports Leisure and Games");
                arrayCategoryList.add("Home and Garden");
                arrayCategoryList.add("Movies ,Books and Music");
                arrayCategoryList.add("Fashion and Accessories");


                dialogSpnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selectedPriceType = arrayListPrice.get(position);
                        Log.d("msg", "currency " + selectedPriceType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                dialogDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uploadimage();
                        dialog.dismiss();


                    }
                });

                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                //GetCategoryList();


                dialog.show();
            }

        });


        btnRetakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SaleStuffFragment()).commit();
                materialDesignFAM.close(true);
            }
        });


    }


    private void AddToRoyality() {


/*
        http://web-medico.com/web1/pic_citi/Api/add_royalty_pic.php?user_id=25&image=abc.jpg
*/
        Log.d("msg", "add to royality file image path  " + imagePath + " usr id " + Utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId));


        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Ion.with(CameraImagePriview.this)
                .load("http://web-medico.com/web1/pic_citi/Api/add_royalty_pic.php?")
                .progressDialog(progressDialog)
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId))
                .setMultipartFile("image", new File(imagePath))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        Log.d("msg", "add to royality res " + result);

                        try {

                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("successful");
                            if (status.equalsIgnoreCase("true")) {

                                Toast.makeText(CameraImagePriview.this, "you have send " + jsonObject.getString("total_royalty_pics_uploaded") + "in royality ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CameraImagePriview.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(CameraImagePriview.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                    }
                });

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

    public void Uploadimage() {
        // http://web-medico.com/web1/pic_citi/Api/image_upload_new.php?user_id=25&image=1.jpg&price=20$&
        // description=xyzzz&location=abc&category_id=1&currency=USD

        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Ion.with(getApplicationContext())

                .load(Constant.Base_URL + "image_upload_new_lat_long.php?")
                .progressDialog(progressDialog)
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId))
                .setMultipartFile("image", new File(imagePath))
                .setMultipartParameter("price", dialogPrice.getText().toString())
                .setMultipartParameter("description", dialogDesc.getText().toString())
                .setMultipartParameter("location", dialogLoc.getText().toString())
                .setMultipartParameter("name", dialogTitle.getText().toString())
                .setMultipartParameter("category_id", selectedCategoryName)
                .setMultipartParameter("currency", selectedPriceType)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("msg", "cameraimageprivew res " + result);
                        Toast.makeText(CameraImagePriview.this, "Succesfully Upload", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intentHome = new Intent(CameraImagePriview.this, MainActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentHome);
                        finish();
                    }
                });

    }

    public void GetCategoryList() {

/*
        http://web-medico.com/web1/pic_citi/Api/get_category_list.php
*/


        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Ion.with(getApplicationContext())

                .load(Constant.Base_URL + "get_category_list.php")
                .progressDialog(progressDialog)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {


                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("true")) {
                                JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {

                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                    String categoryid = jsonObjectData.getString("category_id");
                                    String categoryName = jsonObjectData.getString("category_name");

                                    categoryIdArray.add(categoryid);
                                    categoryNameArray.add(categoryName);
                                }


                                // ArrayAdapter<String> spnerCategory = new ArrayAdapter<String>(CameraImagePriview.this, R.layout.spinner_item_view, categoryNameArray);
                                // dialogCategory.setAdapter(spnerCategory);

                                dialogCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        selectedCategoryName = categoryIdArray.get(position);
                                        Log.d("msg", "selected category  " + selectedCategoryName);

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
                });
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_filter:

                Toast.makeText(CameraImagePriview.this, "click", Toast.LENGTH_SHORT).show();

              /*  Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
*/
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setDataAndType(Uri.parse(imagePath), "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, FILTER_IMAGE);

                break;

            case R.id.btn_canvastpic:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(CameraImagePriview.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == FILTER_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ivCapture.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}