package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.RoyaltyPicGallery;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

public class RoyaltyPicDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public Utils utils;
    public TextView tvImageOwnerName, tvUser;
    public Button btnSendtoGallery, btnCanvasRoyaltyPic, btnPasteMe, btnDetais;
    public String strpaypalEmail, strImageId, strImageUserName, strRoyaltyImage, strUserId, strUserProfile;
    public String imgID, imgTitle, imgOriginal, imgCanvas, imgSizePrice;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.CLIENT_ID);
    public Double amountPayStr;
    private ImageView header_iv_back, ivUser_image, ivBack, ivRoyaltyImage;
    public Dialog dialog;
    public RoyaltyPicGallery details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royalty_pic_details);
        utils = new Utils(RoyaltyPicDetailsActivity.this);
        amountPayStr = 1.00;
        ivRoyaltyImage = (ImageView) findViewById(R.id.iv_royalty_image_detail);
        tvImageOwnerName = (TextView) findViewById(R.id.activity_royalty_gallery_details_image_owenerName);
        btnSendtoGallery = (Button) findViewById(R.id.btn_send_my_gallery);
        btnCanvasRoyaltyPic = (Button) findViewById(R.id.btn_canvas_royalty_pic);
        btnPasteMe = (Button) findViewById(R.id.btn_royalty_paste_me);
        btnDetais = (Button) findViewById(R.id.btn_royalty_img_detail);
        header_iv_back = (ImageView) findViewById(R.id.header_iv_back);
        ivRoyaltyImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        init();
    }

    private void init() {
        if (getIntent().getExtras() != null) {
            Gson gson = new Gson();
            String strObj = getIntent().getExtras().getString("rolaltyimagedetails");
            details = gson.fromJson(strObj, RoyaltyPicGallery.class);
            imgID = details.getId();
            strUserId = details.getUserid();
            imgTitle = details.getName();
            imgOriginal = details.getImage();
            strUserProfile = details.getUser_profile();
            strpaypalEmail = details.getPaypal_email();
            Log.d("IMAGE@!", "" + imgTitle);
            Toast.makeText(this, "" + strpaypalEmail, Toast.LENGTH_SHORT).show();
            Glide.with(getApplicationContext()).load(imgOriginal).placeholder(R.drawable.ic_placeholder).into(ivRoyaltyImage);
            tvImageOwnerName.setText("Name : " + imgTitle);
        }
        header_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSendtoGallery.setOnClickListener(this);
        btnCanvasRoyaltyPic.setOnClickListener(this);
        btnPasteMe.setOnClickListener(this);
        btnDetais.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_send_my_gallery:
                onBuyPressed(v);
                break;
            case R.id.btn_canvas_royalty_pic:
                Intent intent = new Intent(getApplicationContext(), CanvasGalleryImage.class);
                intent.putExtra("canvasID", imgID);
                intent.putExtra("canvasImageTitle", imgTitle);
                intent.putExtra("canvasImageOriginal", imgOriginal);
                intent.putExtra("canvasImageCanvasType", imgCanvas);
                intent.putExtra("canvasImageSizeAndPrice", imgSizePrice);
                startActivity(intent);
                Toast.makeText(RoyaltyPicDetailsActivity.this, "Canvas Royalty Pic", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_royalty_paste_me:
                Toast.makeText(RoyaltyPicDetailsActivity.this, "Paste Me", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_royalty_img_detail:
                dialog = new Dialog(RoyaltyPicDetailsActivity.this);
                dialog.setContentView(R.layout.dialogue_royalty_images_info);
                ivUser_image = (ImageView) dialog.findViewById(R.id.ivUser_image);
                tvUser = (TextView) dialog.findViewById(R.id.tvName);
                ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
                tvUser.setText("Image Owner Name : " + imgTitle);
                Picasso.with(RoyaltyPicDetailsActivity.this).load(details.getUser_profile()).placeholder(R.drawable.ic_placeholder).into(ivUser_image);
                dialog.show();
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    public void onBuyPressed(View pressed) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amountPayStr), "USD", "Pic Citi",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        JSONObject object_payment = new JSONObject();
        try {
            object_payment.put("email", strpaypalEmail);
            object_payment.put("amount", amountPayStr);
            object_payment.put("per", 50);
            object_payment.put("project", "piccity");
            Log.d("STRING_PAYMENT", "" + object_payment.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //payment.custom("{email:archirayan3@gmail.com,amount: 1,per: 50,project:piccity}");
        payment.custom(object_payment.toString());
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    String Respond = confirm.toJSONObject().toString(4);

                    JSONObject jsonObject = new JSONObject(Respond);
                    JSONObject main = jsonObject.getJSONObject("response");

                    if (main.getString("state").equalsIgnoreCase("approved")) {
                        File direct = new File(Environment.getExternalStorageDirectory() + "/Camera");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }


                        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(strRoyaltyImage);
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
                        Toast.makeText(RoyaltyPicDetailsActivity.this, "Payment Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RoyaltyPicDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
