package com.example.archi1.piccity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Adapter.FeedbackAdapter;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.ArtGallery;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

public class ArtGalleryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.CLIENT_ID);
    public Utils utils;
    public ImageView imageView, ivMessage, ivInfo, ivSendFeedback;
    public ImageView header_product_iv_back, header_alist_msg, header_alist_feedback;
    public TextView txtName, txtPrice, tvDescription;
    public String useridd, imgID, imgPrice, imgPath, imgDescription, imgLocation, name, username, userId, strPaypalEmail;
    public PayPalPayment payment;
    public Double amountPayStr;
    public Double amountGalleryImage = 0.0;
    public ArrayList<ArtGallery> arrayList;
    public FeedbackAdapter feedbackAdapter;
   // public Toolbar toolbar;
    public Dialog dialog, dialog_feedback;
    public ArtGallery details;
    public RecyclerView feedbackRecyclerview;
    public EditText edtFeedback;
    private Bitmap rotatedBitmap;

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
        txtName = (TextView) findViewById(R.id.activity_art_gallery_details_txt_Name);
        tvDescription = (TextView) findViewById(R.id.activity_art_gallery_details_txt_description);
        txtPrice = (TextView) findViewById(R.id.activity_art_gallery_details_txt_price);
        Button btnContact = (Button) findViewById(R.id.btnContact);
        ImageView ivEdit = (ImageView) findViewById(R.id.ivEdit);
        ivMessage = (ImageView) findViewById(R.id.header_alist_share);
        ivInfo = (ImageView) findViewById(R.id.header_alist_info);
        header_alist_msg = (ImageView) findViewById(R.id.header_alist_msg);
        header_product_iv_back = (ImageView) findViewById(R.id.header_product_iv_back);
        header_alist_feedback = (ImageView) findViewById(R.id.header_alist_feedback);
        ivEdit.setOnClickListener(this);
        header_alist_feedback.setOnClickListener(this);
        useridd = Utils.ReadSharePrefrence(ArtGalleryDetailsActivity.this, Constant.USER_ID);
        init();

        if (userId.equalsIgnoreCase(useridd)) {
            ivEdit.setVisibility(View.VISIBLE);
        } else {
            ivEdit.setVisibility(View.GONE);
        }

        if (userId.equalsIgnoreCase(useridd)) {
            btnContact.setText("Mark as Sold");
        } else {
            btnContact.setText("Chat With " + username);
        }

        if (btnContact.getText().toString().equalsIgnoreCase("Mark as Sold")) {
            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoldProduct();
                }
            });
        } else {
            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_msg = new Intent(ArtGalleryDetailsActivity.this, ChatActivity.class);
                    intent_msg.putExtra("receipt_id", userId);
                    startActivity(intent_msg);
                }
            });
        }
        new RotateBitmapAsy().execute();
    }

    private void init() {
        if (getIntent().getExtras() != null) {
            Gson gson = new Gson();
            String strDetails = getIntent().getExtras().getString("artgallerydetails");
            details = gson.fromJson(strDetails, ArtGallery.class);
            imgID = details.getId();
            imgDescription = details.getDescription();
            imgPath = details.getImage();
            imgPrice = details.getPrice();
            name = details.getName();
            imgLocation = details.getLocation();
            username = details.getUsername();
            userId = details.getUserId();
            String currency = details.getCurrency();
            strPaypalEmail = details.getPaypalEmail();
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            txtName.setText("Name: " + name);
            txtPrice.setText("Price: " + imgPrice + " " + currency);
            tvDescription.setText("Description : " + imgDescription);
            header_alist_msg.setOnClickListener(new View.OnClickListener() {
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
        }

        ivMessage.setOnClickListener(this);
        ivInfo.setOnClickListener(this);
        header_product_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    public void onBuyPressed(View v) {

        payment = new PayPalPayment(new BigDecimal(amountGalleryImage), "USD", "Pic Citi",
                PayPalPayment.PAYMENT_INTENT_SALE);

        JSONObject object_payment = new JSONObject();
        try {
            object_payment.put("email", strPaypalEmail);
            object_payment.put("amount", amountGalleryImage);
            object_payment.put("per", 20);
            object_payment.put("project", "piccity");
            Log.d("STRING_PAYMENT", "" + object_payment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, PaymentActivity.class);
        //payment.custom("{\"email\": \"archirayan9@gmail.com\",\"amount\": 50,\"per\": 10,\"project\": \"piccity\"}");
        payment.custom(object_payment.toString());
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_alist_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgPath);
                startActivity(Intent.createChooser(shareIntent, "sharing Image"));
                break;

            case R.id.header_alist_info:

                dialog = new Dialog(ArtGalleryDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image_more_info);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();

                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams wlp = window.getAttributes();

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
                header_alist_msg.setOnClickListener(this);
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
                i.putExtra("userid", userId);
                startActivity(i);
                break;

            case R.id.header_alist_feedback:
                dialog_feedback = new Dialog(ArtGalleryDetailsActivity.this);
                dialog_feedback.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_feedback.setContentView(R.layout.dialog_feedback_show);
                dialog_feedback.setCancelable(true);
                dialog_feedback.setCanceledOnTouchOutside(true);
                dialog_feedback.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog_feedback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                Window feedback_window = dialog_feedback.getWindow();
                feedback_window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams wlp1 = feedback_window.getAttributes();
                feedback_window.setAttributes(wlp1);
                edtFeedback = (EditText) dialog_feedback.findViewById(R.id.activity_art_gallery_details_edfeedback);
                ivSendFeedback = (ImageView) dialog_feedback.findViewById(R.id.activity_art_gallery_details_imgfeedbacksend);
                feedbackRecyclerview = (RecyclerView) dialog_feedback.findViewById(R.id.dialog_feedback_recycler);
                dialog_feedback.show();
                new GetFeedBackDetails(imgID).execute();
                ivSendFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strFeedBack = edtFeedback.getText().toString();
                        new SendFeedback(useridd, imgID, strFeedBack).execute();
                        edtFeedback.setText("");
                        new GetFeedBackDetails(imgID).execute();
                    }
                });
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

                    new DeleteImageRecord().execute();
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

    private class DeleteImageRecord extends AsyncTask<String, String, String> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ArtGalleryDetailsActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String delteurl = Constant.Base_URL + "delete_alluser_image.php?id=" + imgID;
            return Utils.getResponseofGet(delteurl);
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            Log.d("POST", "" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private class RotateBitmappAsy extends AsyncTask<Void, Void, String> {

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

    private class RotateBitmappAsyForFacebook extends AsyncTask<Void, Void, String> {

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

    private class RotateBitmappAsyForLinkdin extends AsyncTask<Void, Void, String> {

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

    private class RotateBitmappAsyForInstagram extends AsyncTask<Void, Void, String> {

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

    private class RotateBitmapAsy extends AsyncTask<Void, Void, String> {

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

    private class SendFeedback extends AsyncTask<String, String, String> {
        String user_id, imageid, feedback;
        private ProgressDialog pd;

        private SendFeedback(String userid, String imgID, String strFeedBack) {
            this.user_id = userid;
            this.imageid = imgID;
            this.feedback = strFeedBack;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ArtGalleryDetailsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.isShowing();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //http://web-medico.com/web1/pic_citi/Api/feedback.php?user_id=190&img_id=3&feedback=nice%20one
            return Utils.getResponseofGet(Constant.Base_URL + "feedback.php?user_id=" + user_id + "&img_id=" + imageid + "&feedback=" + feedback.replaceAll(" ", "%20"));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONObject feedBackObject = jsonObject.getJSONObject("data");
                    details.setImageFeedBackText(feedBackObject.getString("feedback"));
                    details.setUserId(feedBackObject.getString("user_id"));
                    details.setUsername(feedBackObject.getString("user_name"));
                    details.setUserProfilePic(feedBackObject.getString("user_image"));
                    details.setId(feedBackObject.getString("img_id"));
                    details.setImage(feedBackObject.getString("img"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetFeedBackDetails extends AsyncTask<String, String, String> {
        String imageid;
        private ProgressDialog pd;

        private GetFeedBackDetails(String imageid) {
            this.imageid = imageid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
            pd = new ProgressDialog(ArtGalleryDetailsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArtGallery details = new ArtGallery();
                        JSONObject feedBackObject = jsonArray.getJSONObject(i);
                        details.setImageFeedBackText(feedBackObject.getString("feedback"));
                        details.setImageFeedBackId(feedBackObject.getString("feedback_id"));
                        details.setUserId(feedBackObject.getString("user_id"));
                        details.setUsername(feedBackObject.getString("user_name"));
                        details.setUserProfilePic(feedBackObject.getString("user_image"));
                        details.setId(feedBackObject.getString("img_id"));
                        details.setImage(feedBackObject.getString("image"));
                        arrayList.add(details);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                feedbackAdapter = new FeedbackAdapter(ArtGalleryDetailsActivity.this, arrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ArtGalleryDetailsActivity.this);
                feedbackRecyclerview.setLayoutManager(mLayoutManager);
                feedbackRecyclerview.setItemAnimator(new DefaultItemAnimator());
                feedbackRecyclerview.setAdapter(feedbackAdapter);
                feedbackAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ArtGalleryDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/get_user_feedback.php?img_id=3
            return Utils.getResponseofGet(Constant.Base_URL + "get_user_feedback.php?img_id=" + imageid);
        }
    }
}
