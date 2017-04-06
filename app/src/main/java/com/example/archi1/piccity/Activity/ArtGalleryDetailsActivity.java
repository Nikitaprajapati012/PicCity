package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Chat.AlistChat;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Fragment.ArtGalleryFragment;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

public class ArtGalleryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public Utils utils;
    public RelativeLayout relativeImagelayout;
    public ImageView imageView, ivMessage, ivInfo;
    public ImageView header_product_iv_back;
    public TextView txtName, txtPrice;
    public String imgID, imgPrice, imgPath, imgDescription, imgLocation, name, username, userid;
    public TextView txtProductMoreInfo, txtProductShare;
    private Bitmap rotatedBitmap;
    private TextView tvDescription;
    public PayPalPayment payment;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.CLIENT_ID);
    public Double amountPayStr;
    public Double amountGalleryImage = 0.0;
    public Toolbar toolbar;
    public Dialog dialog;

    public static Bitmap RotateBitmap(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_gallery_details);


        utils = new Utils(ArtGalleryDetailsActivity.this);
        imageView = (ImageView) findViewById(R.id.activity_art_gallery_details_image);
        // relativeImagelayout = (RelativeLayout) findViewById(R.id.activity_art_gallery_details);
        txtName = (TextView) findViewById(R.id.activity_art_gallery_details_txt_Name);
        txtPrice = (TextView) findViewById(R.id.activity_art_gallery_details_txt_price);
        Button btnContact = (Button) findViewById(R.id.btnContact);
        ImageView ivEdit = (ImageView) findViewById(R.id.ivEdit);
        ivMessage = (ImageView) findViewById(R.id.header_alist_share);
        ivInfo = (ImageView) findViewById(R.id.header_alist_info);
        header_product_iv_back = (ImageView)findViewById(R.id.header_product_iv_back);

        ivEdit.setOnClickListener(this);
        String useridd = Utils.ReadSharePrefrence(ArtGalleryDetailsActivity.this, Constant.USER_ID);
        init();

        if (userid.equalsIgnoreCase(useridd)) {
            ivEdit.setVisibility(View.VISIBLE);
        } else {
            ivEdit.setVisibility(View.GONE);
        }

        if (userid.equalsIgnoreCase(useridd)) {
            btnContact.setText("Make as Sold");

        } else {
            btnContact.setText("Chat With " + username);

        }

        if(btnContact.getText().toString().equalsIgnoreCase("Make as Sold"))
        {
            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoldProduct();
                }
            });
        }
        else
        {
            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ArtGalleryDetailsActivity.this, "Hii...", Toast.LENGTH_SHORT).show();
                    Intent chatIntent = new Intent(ArtGalleryDetailsActivity.this,AlistChat.class);
                    startActivity(chatIntent);
                }
            });
        }

        new RotateBitmapAsy().execute();

    }

    private void init() {

        if (getIntent().getExtras() != null) {

            imgID = getIntent().getExtras().getString("id");
            imgDescription = getIntent().getExtras().getString("description");
            imgPath = getIntent().getExtras().getString("image");
            imgPrice = getIntent().getExtras().getString("price");
            name = getIntent().getExtras().getString("name");
            imgLocation = getIntent().getExtras().getString("location");
            username = getIntent().getExtras().getString("username");
            userid = getIntent().getExtras().getString("userid");
            String currency = getIntent().getExtras().getString("currency");
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
          //Picasso.with(getApplicationContext()).load(imgPath).placeholder(R.drawable.ic_placeholder).into(imageView);

            txtName.setText("Name: " + name);
            //tvDescription.setText("Description : " + imgDescription);
            txtPrice.setText("Price: " + imgPrice + " " + currency);

        }

//        ivProductClose.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivInfo.setOnClickListener(this);
        header_product_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                /*Intent i = new Intent(ArtGalleryDetailsActivity.this,ArtGalleryFragment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);*/
            }
        });
        //txtProductMoreInfo.setOnClickListener(this);
    }

    public void onBuyPressed(View v) {

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
        List<ResolveInfo> activityList;
        Intent share_Intent;
        PackageManager pm;
        switch (v.getId()) {

            case R.id.header_alist_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgPath);
                startActivity(Intent.createChooser(shareIntent, "sharing Image"));
                break;

            case R.id.header_alist_info:
                //            ivProductClose.setVisibility(View.GONE);
                //            txtProductMoreInfo.setVisibility(View.GONE);
                //            txtProductShare.setVisibility(View.GONE);


                dialog = new Dialog(ArtGalleryDetailsActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image_more_info);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();

                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams wlp = window.getAttributes();

                //window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setAttributes(wlp);


                ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_product_close);
                TextView tvbuy = (TextView) dialog.findViewById(R.id.txt_product_buy);
                TextView tvprice = (TextView) dialog.findViewById(R.id.txt_product_price);
                TextView tvlocation = (TextView) dialog.findViewById(R.id.dialog_txt_location);
                TextView tvName = (TextView) dialog.findViewById(R.id.dialog_name);
                TextView tvDesc = (TextView) dialog.findViewById(R.id.dialog_description);

                ImageView ivFb = (ImageView) dialog.findViewById(R.id.iv_fb);
                ImageView ivinsta = (ImageView) dialog.findViewById(R.id.iv_instagram);
                ImageView ivin = (ImageView) dialog.findViewById(R.id.iv_in);
                ImageView ivwtsapp = (ImageView) dialog.findViewById(R.id.iv_wtsapp);


                ivFb.setOnClickListener(this);
                ivinsta.setOnClickListener(this);
                ivin.setOnClickListener(this);
                ivwtsapp.setOnClickListener(this);


                tvbuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amountGalleryImage = Double.parseDouble(imgPrice.toString());
                        amountPayStr = 15.00;

                        onBuyPressed(v);
                        Intent intent = new Intent(ArtGalleryDetailsActivity.this, PayPalService.class);
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                        startService(intent);
                    }
                });

                tvprice.setText("Price : " + imgPrice);
                tvlocation.setText("Location : " + imgLocation);
                tvName.setText("Name : " + name);
                tvDesc.setText("" + imgDescription);


                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return true;
                    }
                });
                break;


            case R.id.iv_wtsapp:
                new RotateBitmappAsy().execute();
                break;


            case R.id.iv_fb:
                new RotateBitmappAsyForFacebook().execute();
                break;


            case R.id.iv_in:
                new RotateBitmappAsyForLinkdin().execute();
                break;

            case R.id.iv_instagram:
                new RotateBitmappAsyForInstagram().execute();
                break;

            case R.id.iv_product_close:
                Toast.makeText(ArtGalleryDetailsActivity.this, "click", Toast.LENGTH_SHORT).show();
                finish();
                break;


            case R.id.ivEdit:
                Toast.makeText(ArtGalleryDetailsActivity.this, "click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ArtGalleryDetailsActivity.this, EditArtListActivity.class);
                i.putExtra("id", imgID);
                i.putExtra("description", imgDescription);
                i.putExtra("price", imgPrice);
                i.putExtra("location", imgLocation);
                i.putExtra("image", imgPath);
                i.putExtra("username", username);
                i.putExtra("name", name);
                i.putExtra("userid", userid);
                startActivity(i);

                break;

        }
    }

    public void SoldProduct() {

/*
        http://web-medico.com/web1/pic_citi/Api/sold_item.php?id=112&user_id=25&sold_status=1
*/
        Log.d("msg", "img id sold " + imgID);
        Ion.with(ArtGalleryDetailsActivity.this)
                .load(Constant.Base_URL + "sold_item.php?")
                .setMultipartParameter("user_id", Utils.ReadSharePrefrence(ArtGalleryDetailsActivity.this, Constant.UserId))
                .setMultipartParameter("id", imgID)
                .setMultipartParameter("sold_status", "1")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        Log.d("msg", "sold res" + result);


                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("true")) {
                                Toast.makeText(ArtGalleryDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ArtGalleryDetailsActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(ArtGalleryDetailsActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    class RotateBitmappAsy extends AsyncTask<Void, Void, String> {

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


            Intent intent = new Intent(Intent.ACTION_SEND);

            String path = null;

            try {
                path = MediaStore.Images.Media.insertImage(getContentResolver(), rotatedBitmap, "", null);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(intent, "Share image via..."));
        }
    }


    class RotateBitmappAsyForFacebook extends AsyncTask<Void, Void, String> {

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


            Intent intent = new Intent(Intent.ACTION_SEND);

            String path = null;

            try {
                path = MediaStore.Images.Media.insertImage(getContentResolver(), rotatedBitmap, "", null);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            intent.setPackage("com.facebook.katana");
            startActivity(Intent.createChooser(intent, "Share image via..."));
        }
    }


    class RotateBitmappAsyForLinkdin extends AsyncTask<Void, Void, String> {

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


            Intent intent = new Intent(Intent.ACTION_SEND);

            String path = null;

            try {
                path = MediaStore.Images.Media.insertImage(getContentResolver(), rotatedBitmap, "", null);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            intent.setPackage("com.linkedin.android");
            startActivity(Intent.createChooser(intent, "Share image via..."));
        }
    }


    class RotateBitmappAsyForInstagram extends AsyncTask<Void, Void, String> {

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


            Intent intent = new Intent(Intent.ACTION_SEND);

            String path = null;

            try {
                path = MediaStore.Images.Media.insertImage(getContentResolver(), rotatedBitmap, "", null);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            intent.setPackage("com.instagram.android");
            startActivity(Intent.createChooser(intent, "Share image via..."));
        }
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
            imageView.setImageBitmap(rotatedBitmap);
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

                    if (main.getString("state").equalsIgnoreCase("approved")) {
                        File direct = new File(Environment.getExternalStorageDirectory() + "/Camera");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }


                        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(imgPath);
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
                        Toast.makeText(ArtGalleryDetailsActivity.this, "Payment Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ArtGalleryDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
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
}
