package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.Model.SizePrice;
import com.example.archi1.piccity.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static android.R.id.input;
import static com.paypal.android.sdk.payments.PayPalConfiguration.ENVIRONMENT_PRODUCTION;
import static com.paypal.android.sdk.payments.PayPalConfiguration.ENVIRONMENT_SANDBOX;

public class GalleryDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public Utils utils;
    public String imgID, imgOriginal, imgCanvas, imgTitle, imgSizePrice;
    public ImageView ivHeader;
    public TextView tvImgTiitle, tvImgPrice;
    public Spinner spImgSize;
    public ArrayList<SizePrice> arraylistSizePrize;
    public Button imageBuy;
    public RelativeLayout mRelativeSizePrice;
    public String ImagePrice;
    public PayPalPayment payment;
   // public String user_id;
    public Button btnGlryDetailSendToMyGallery, btnGlrDetlaiPasteMe, btnGlrtDetailCanvasMyPic;
   // private static final String CLIENT_ID = "AWuRa4dpStkz11pJBwAXn5Mmoch9AGl_IcALFwQ6Xj5BXrHoVyvbRN3X5T9Q_W9cgC_PMOr0HOL31Lv5";
    String size;
    String price;
    String id;
    public String payment_id;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.CLIENT_ID);
    public Double amountPayStr;
    public Double amountGalleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);

        utils = new Utils(GalleryDetailsActivity.this);


        amountGalleryImage = 2.99;
        amountPayStr = 15.00;

        bindData();

        //For PayPAl
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        arraylistSizePrize = new ArrayList<>();

        if (utils.isConnectingToInternet()) {
            init();

        } else {
            Toast.makeText(GalleryDetailsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void bindData() {


        btnGlrtDetailCanvasMyPic = (Button) findViewById(R.id.activity_gallery_detail_canvas_pic);
        btnGlrDetlaiPasteMe = (Button) findViewById(R.id.activity_gallery_detail_paste_me);
        btnGlryDetailSendToMyGallery = (Button) findViewById(R.id.activity_gallery_detail_send_my_gallery);
        ivHeader = (ImageView) findViewById(R.id.activity_glry_iv_header);
        tvImgPrice = (TextView) findViewById(R.id.activity_glry_img_price);
        tvImgTiitle = (TextView) findViewById(R.id.activity_glry_img_title);
        spImgSize = (Spinner) findViewById(R.id.activity_glry_size_spinner);
        imageBuy = (Button) findViewById(R.id.btn_buy_gallery_image);


        mRelativeSizePrice = (RelativeLayout) findViewById(R.id.activity_gallery_detail_sizeprice_layout);
        mRelativeSizePrice.setVisibility(View.INVISIBLE);


        imageBuy.setOnClickListener(this);
        btnGlryDetailSendToMyGallery.setOnClickListener(this);
        btnGlrDetlaiPasteMe.setOnClickListener(this);
        btnGlrtDetailCanvasMyPic.setOnClickListener(this);
    }

    private void init() {

        if (getIntent().getExtras() != null) {

            imgID = getIntent().getExtras().getString("id");
            imgTitle = getIntent().getExtras().getString("title");
            imgOriginal = getIntent().getExtras().getString("image");
            imgCanvas = getIntent().getExtras().getString("canvasImage");
            imgSizePrice = getIntent().getExtras().getString("sizePrice");

            Log.d("sizePrice", imgSizePrice);
            try {
                JSONArray jsonArray = new JSONArray(imgSizePrice.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    SizePrice sizePrice = new SizePrice();
                    id = jsonObject.getString("id");
                    price = jsonObject.getString("price");
                    size = jsonObject.getString("size");

                    sizePrice.setId(id);
                    sizePrice.setPrice(price);
                    sizePrice.setSize(size);
                    Log.d("vijay", "id :" + id + "\n price :" + price + "\n " + size + "\n ==============");
                    arraylistSizePrize.add(sizePrice);
                }
                Log.d("vijay", "array size :" + arraylistSizePrize.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            tvImgTiitle.setText("Name:" + imgTitle);
            Glide.with(getApplicationContext()).load(imgOriginal).placeholder(R.drawable.ic_placeholder).into(ivHeader);
            ArrayList<String> imageSize = new ArrayList<>();
            for (int i = 0; i < arraylistSizePrize.size(); i++) {
                imageSize.add(arraylistSizePrize.get(i).getSize());

            }

            ArrayAdapter<String> spinnerArrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imageSize);
            spinnerArrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spImgSize.setAdapter(spinnerArrayadapter);


        }

        spImgSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImagePrice = arraylistSizePrize.get(position).getPrice();
                tvImgPrice.setText(ImagePrice);
                String payPrice = ImagePrice.substring(1);
                amountPayStr = Double.valueOf(payPrice);
                //tvImgPrice.setText("Price :"+arraylistSizePrize.get(position).getPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.


         payment = new PayPalPayment(new BigDecimal(amountGalleryImage), "USD", "Say Post",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_gallery_image:
                break;

            case R.id.activity_gallery_detail_send_my_gallery:

                onBuyPressed(v);
                Toast.makeText(getApplicationContext(), "You Clicked Send To My Gallery", Toast.LENGTH_SHORT).show();
                break;

            case R.id.activity_gallery_detail_canvas_pic:

                mRelativeSizePrice.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), CanvasGalleryImage.class);
                intent.putExtra("canvasID", imgID);
                intent.putExtra("canvasImageTitle", imgTitle);
                intent.putExtra("canvasImageOriginal", imgOriginal);
                intent.putExtra("canvasImageCanvasType", imgCanvas);
                intent.putExtra("canvasImageSizeAndPrice", imgSizePrice);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Please Select a Size and Price", Toast.LENGTH_SHORT).show();
                break;

            case R.id.activity_gallery_detail_paste_me:
                Toast.makeText(getApplicationContext(), "Paste Me", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {


            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExampleFull", payment.toJSONObject().toString());
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    String Respond = confirm.toJSONObject().toString(4);

                    JSONObject jsonObject = new JSONObject(Respond);
                    JSONObject main = jsonObject.getJSONObject("response");
                    final String transactionId = jsonObject.getJSONObject("response").getString("id");
                    Log.d("transactionId", transactionId);
                    new AddPayamentInfo(transactionId).execute();

                   // payment_id = main.getString("id");
                   //// Toast.makeText(GalleryDetailsActivity.this, "id====>>>"+id, Toast.LENGTH_SHORT).show();
                    if (main.getString("state").equalsIgnoreCase("approved")) {
                        File direct = new File(Environment.getExternalStorageDirectory() + "/Camera");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }


                        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(imgOriginal);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        String name = Environment.getExternalStorageDirectory().getAbsolutePath();

                        name += "/piccity/";
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false).setTitle("Piccity")
                                .setDescription("Something Usefull")
                                .setDestinationInExternalPublicDir(name, "picicty.jpg");
                        mgr.enqueue(request);

                        Log.d("PaymentRespond", Respond);
                        Toast.makeText(GalleryDetailsActivity.this, "Payment Succesfully", Toast.LENGTH_SHORT).show();



                    } else {
                        Toast.makeText(GalleryDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    class AddPayamentInfo extends AsyncTask<String,String,String> {
        public String transactionId;
        ProgressDialog pd;
        String user_id;
        //HashMap<String,String> hashMap;
        HashMap<String, String> hashMap = new HashMap<>();

        public AddPayamentInfo(String transactionId) {
            this.transactionId = transactionId;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_id =  Utils.ReadSharePrefrence(GalleryDetailsActivity.this,Constant.USER_ID);
            pd = new ProgressDialog(GalleryDetailsActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            hashMap.put("user_id",user_id);
            hashMap.put("imgid",imgID);
            hashMap.put("img",imgOriginal);
            hashMap.put("imgprice",ImagePrice);
            hashMap.put("transaction_id",transactionId);
            pd.isShowing();
        }

        @Override
        protected String doInBackground(String... params) {

            return Utils.getResponseofPost(Constant.Base_URL+"image.php", hashMap);
           // return utils.getResponseofGet(Constant.Base_URL + "image.php"+"user_id="+user_id+"&imgid="+imgID+"&img="+imgOriginal+"&imgprice="+imgSizePrice+"&transaction_id="+payment_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Re",s);
            Log.d("Re",user_id);
            pd.dismiss();

            Toast.makeText(GalleryDetailsActivity.this, "fgh"+user_id, Toast.LENGTH_SHORT).show();
            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    //Toast.makeText(GalleryDetailsActivity.this, "Successffully Inserted...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(GalleryDetailsActivity.this, "Error"+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}