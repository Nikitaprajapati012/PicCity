package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.math.BigDecimal;

import static com.example.archi1.piccity.R.id.imageView;

public class RoyaltyPicDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public Utils utils;
    public ImageView ivRoyaltyImage;
    public TextView tvImageOwnerName;
    public Button btnSendtoGallery,btnCanvasRoyaltyPic,btnPasteMe,btnDetais;
    public String strImageId,strImageUserName,strRoyaltyImage,strUserId,strUserProfile;
    private static final String CLIENT_ID = "AWuRa4dpStkz11pJBwAXn5Mmoch9AGl_IcALFwQ6Xj5BXrHoVyvbRN3X5T9Q_W9cgC_PMOr0HOL31Lv5";

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(CLIENT_ID);
    public Double amountPayStr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royalty_pic_details);

        utils = new Utils(RoyaltyPicDetailsActivity.this);

        amountPayStr = 1.00;

        ivRoyaltyImage=(ImageView)findViewById(R.id.iv_royalty_image_details);
        tvImageOwnerName=(TextView)findViewById(R.id.activity_royalty_gallery_details_image_owenerName);
        btnSendtoGallery=(Button)findViewById(R.id.btn_send_my_gallery);
        btnCanvasRoyaltyPic=(Button)findViewById(R.id.btn_canvas_royalty_pic);
        btnPasteMe=(Button)findViewById(R.id.btn_royalty_paste_me);
        btnDetais=(Button)findViewById(R.id.btn_royalty_img_detail);


        init();


    }

    private void init() {


        if (getIntent().getExtras() != null){

            strImageId = getIntent().getExtras().getString("id");
            strUserId=getIntent().getExtras().getString("user_id");
            strImageUserName=getIntent().getExtras().getString("user_name");
            strRoyaltyImage=getIntent().getExtras().getString("image");
            strUserProfile = getIntent().getExtras().getString("user_image");

            ivRoyaltyImage.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(getApplicationContext()).load(strRoyaltyImage).placeholder(R.drawable.ic_placeholder).into(ivRoyaltyImage);
            tvImageOwnerName.setText("Name:"+strImageUserName);
        }

        btnSendtoGallery.setOnClickListener(this);
        btnCanvasRoyaltyPic.setOnClickListener(this);
        btnPasteMe.setOnClickListener(this);
        btnDetais.setOnClickListener(this);


    }

    /**/

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.btn_send_my_gallery:


                onBuyPressed(v);
                break;


            case R.id.btn_canvas_royalty_pic:

                Intent intent =new Intent(getApplicationContext(),CanvasRoyaltyImage.class);
                intent.putExtra("canvasRoyaltyImageId",strImageId);
                intent.putExtra("canvasRoyaltyUserId",strUserId);
                intent.putExtra("canvasRoyaltyUserName",strImageUserName);
                intent.putExtra("canvasRoyaltyImage",strRoyaltyImage);
                startActivity(intent);

                Toast.makeText(RoyaltyPicDetailsActivity.this, "Canvas Royalty Pic", Toast.LENGTH_SHORT).show();

                break;

            case R.id.btn_royalty_paste_me:
                Toast.makeText(RoyaltyPicDetailsActivity.this, "Paste Me", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_royalty_img_detail:
                final Dialog dialog = new Dialog(RoyaltyPicDetailsActivity.this,R.style.MyDialog);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_royalty_images_info);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                WindowManager.LayoutParams wlp = window.getAttributes();
                window.setAttributes(wlp);

                ImageView ivUser_image = (ImageView)dialog.findViewById(R.id.ivUser_image);
                TextView tvUser = (TextView)dialog.findViewById(R.id.tvName);
                ImageView ivBack = (ImageView)dialog.findViewById(R.id.ivBack);
                tvUser.setText("Name : "+strImageUserName);
                Toast.makeText(this, ""+strUserProfile, Toast.LENGTH_SHORT).show();

                Picasso.with(this)
                        .load(strUserProfile)
                        .placeholder(R.drawable.ic_placeholder) //this is optional the image to display while the url image is downloading
                        //this is also optional if some error has occurred in downloading the image this image would be displayed
                        .into(ivUser_image);
               // Glide.with(getApplicationContext()).load(strUserProfile).placeholder(R.drawable.ic_placeholder).into(ivUser_image);
                dialog.show();
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

               // onBackPressed(dialog.dismiss(););
                break;

        }

    }

    /* switch (v.getId()){
                case R.id.txt_royalty_product_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM,imgPath);
                startActivity(Intent.createChooser(shareIntent,"sharing Image"));
    }*/

    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.


        PayPalPayment payment = new PayPalPayment(new BigDecimal(amountPayStr), "USD", "Say Post",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null)
            {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    String Respond = confirm.toJSONObject().toString(4);

                    JSONObject jsonObject = new JSONObject(Respond);
                    JSONObject main = jsonObject.getJSONObject("response");


                    if (main.getString("state").equalsIgnoreCase("approved")){
                        File direct = new File(Environment.getExternalStorageDirectory() +"/Camera");
                        if (!direct.exists()){
                            direct.mkdirs();
                        }


                        DownloadManager mgr = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(strRoyaltyImage);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        String name = Environment.getExternalStorageDirectory().getAbsolutePath();

                        name += "/piccity/" ;
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false).setTitle("Piccity")
                                .setDescription("Something Usefull")
                                .setDestinationInExternalPublicDir(name,"picicty.jpg");
                        mgr.enqueue(request);

                        Log.d("PaymentRespond",Respond);
                        Toast.makeText(RoyaltyPicDetailsActivity.this, "Payment Succesfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RoyaltyPicDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.i("paymentExample", "The user canceled.");
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
