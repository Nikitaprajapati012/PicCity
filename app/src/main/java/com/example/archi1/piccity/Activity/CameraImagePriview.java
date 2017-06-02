package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CameraImagePriview extends AppCompatActivity implements View.OnClickListener {
    public EditText dialogPrice, dialogDesc, dialogLoc, dialogTitle;
    public Button dialogDone, dialogCancel;
    public Spinner dialogSpnerPrice, dialogCategory;
    public ArrayList<String> arrayListPrice;
    private String imagePath, strPrice, strDesc, strLoc, strTitle;
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
        Utils utils = new Utils(this);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        btnCanvas = (FloatingActionButton) findViewById(R.id.btn_Image_makeCanvas);
        btnImagePost = (FloatingActionButton) findViewById(R.id.btn_Image_Post);
        btnRetakeImage = (FloatingActionButton) findViewById(R.id.btn_retake_image);
        btnFIlter = (FloatingActionButton) findViewById(R.id.btn_filter);
        btnCanvasPic = (FloatingActionButton) findViewById(R.id.btn_canvastpic);
        btnFIlter.setOnClickListener(this);

        if (utils.isConnectingToInternet())
        {
            GetCategoryList();
            new getCurrencyCode().execute();
        }
        materialDesignFAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                }
            }
        });
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String status = bundle.getString("status");
            if (status.equalsIgnoreCase("gallery")) {
                String galleryImagePath = getIntent().getStringExtra("GalleryImagePath");
                Toast.makeText(this, "galleryImagePath" + imagePath, Toast.LENGTH_SHORT).show();
                Bitmap myBitmap = BitmapFactory.decodeFile(galleryImagePath);
                ivCapture.setImageBitmap(myBitmap);
            } else {
                if (bundle.containsKey("path")) {
                    imagePath = bundle.getString("path", "");
                    activityStatus = bundle.getString("ActivitStatus");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Toast.makeText(this, "IMAGEPATH_GET" + imagePath, Toast.LENGTH_SHORT).show();
                    Bitmap newBitmap = RotateBitmap(myBitmap, 90f);
                    ivCapture.setImageBitmap(newBitmap);
                }
            }
        }
/*        if (activityStatus.equalsIgnoreCase("RoyaltyPicFragment")) {
            // hide buttons
            btnImagePost.setVisibility(View.GONE);
            btnCanvasPic.setVisibility(View.GONE);
            btnFIlter.setVisibility(View.GONE);
        } else if (activityStatus.equalsIgnoreCase("SaleStuff")) {
            Toast.makeText(this, "something went to wrong", Toast.LENGTH_SHORT).show();
        }*/


        btnCanvasPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCanvasImage();
                materialDesignFAM.close(true);
            }
        });

        btnCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //  dialogCategory = (Spinner) dialog.findViewById(R.id.et_);
                dialogSpnerPrice = (Spinner) dialog.findViewById(R.id.spnrPrice);
                dialogCategory = (Spinner) dialog.findViewById(R.id.spcategory);
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(CameraImagePriview.this, R.layout.spinner_item_view, arrayListPrice);
                dialogSpnerPrice.setAdapter(stringArrayAdapter);

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
                ArrayAdapter<String> spCategory = new ArrayAdapter<String>(CameraImagePriview.this, R.layout.spinner_item_view, categoryNameArray);
                dialogCategory.setAdapter(spCategory);
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
                dialogDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strPrice = dialogPrice.getText().toString();
                        strLoc = dialogLoc.getText().toString();
                        strDesc = dialogDesc.getText().toString();
                        strTitle = dialogTitle.getText().toString();
                        // http://web-medico.com/web1/pic_citi/Api/image_upload_new.php?user_id=86&&image=100_1517373497.jpg&price=2&&description=abcde&location=Ahmedabad,gujarat&name=hay&category_id=1&currency=USD
                        try {
                            Ion.with(getApplicationContext())
                                    .load(Constant.Base_URL + "image_upload_new.php?")
                                    .setMultipartParameter("user_id", URLEncoder.encode(Utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId), "UTF-8"))
                                    .setMultipartParameter("price", URLEncoder.encode(strPrice, "UTF-8"))
                                    .setMultipartParameter("description", URLEncoder.encode(strDesc, "UTF-8"))
                                    .setMultipartParameter("location", URLEncoder.encode(strLoc, "UTF-8"))
                                    .setMultipartParameter("name", URLEncoder.encode(strTitle, "UTF-8"))
                                    .setMultipartParameter("category_id", selectedCategoryName)
                                    .setMultipartParameter("currency", selectedPriceType)
                                    .setMultipartFile("image", new File(imagePath))
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            Log.d("JSONRESULT@@" + "", result);
                                            Toast.makeText(CameraImagePriview.this, "Add Alist Successfully....", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            Log.d("IMAGEPATH@", "" + imagePath);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private void AddToRoyality() {
/*
        http://web-medico.com/web1/pic_citi/Api/add_royalty_pic.php?user_id=25&image=abc.jpg
*/
        Log.d("imagePath_Royalty", "" + imagePath);
        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Ion.with(CameraImagePriview.this)
                .load("http://web-medico.com/web1/pic_citi/Api/add_royalty_pic.php?")
                .progressDialog(progressDialog)
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(getApplicationContext(), Constant.USER_ID))
                .setMultipartFile("image", new File(imagePath))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Toast.makeText(CameraImagePriview.this, "Add Royalty Image Successfully...", Toast.LENGTH_SHORT).show();
                        Log.d("msg", "@@" + result);
                        progressDialog.dismiss();
                    }
                });
    }

    public void GetCategoryList() {

/*
        http://web-medico.com/web1/pic_citi/Api/get_category_list.php
*/
        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        categoryIdArray = new ArrayList<>();
        categoryNameArray = new ArrayList<>();

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

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public void AddCanvasImage()
    {
        Log.d("imagePath_Royalty", "" + imagePath);
        final ProgressDialog progressDialog = new ProgressDialog(CameraImagePriview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Ion.with(CameraImagePriview.this)
                .load("http://web-medico.com/web1/pic_citi/Api/add_canvas_images.php?")
                .progressDialog(progressDialog)
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(getApplicationContext(), Constant.USER_ID))
                .setMultipartFile("image", new File(imagePath))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Toast.makeText(CameraImagePriview.this, "Add Canvas Image Successfully...", Toast.LENGTH_SHORT).show();
                        Log.d("msg", "@@" + result);
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_filter:
                if (new File(imagePath).exists()) {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setDataAndType(getImageContentUri(CameraImagePriview.this, new File(imagePath)), "image/*");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, FILTER_IMAGE);
                } else {
                    Toast.makeText(this, "Not exist", Toast.LENGTH_SHORT).show();
                }
                Log.d("ImagePath_Filter",""+imagePath);
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
                        ivCapture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(CameraImagePriview.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == FILTER_IMAGE) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ivCapture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("ERRRO@#", "" + e.toString());
                    }
                }
            }
        } else {
            Toast.makeText(CameraImagePriview.this, "Not edited", Toast.LENGTH_SHORT).show();
        }
    }

    class getCurrencyCode extends AsyncTask<String,String,String>
    {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(CameraImagePriview.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.isShowing();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Url",""+Utils.getResponseofGet(Constant.Base_URL +"currency.php"));
            return Utils.getResponseofGet(Constant.Base_URL +"currency.php");
        }

        @Override
        protected void onPostExecute(String s) {
            arrayListPrice = new ArrayList<>();
            pd.dismiss();
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject getcurobject = jsonArray.getJSONObject(i);
                        String addcurr = getcurobject.getString("code");
                        arrayListPrice.add(addcurr);
                    }
                    Log.d("ARRAYLST",""+arrayListPrice);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }
}
