package com.example.archi1.piccity.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Activity.GalleryDetailsActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.GalleryDetails;
import com.example.archi1.piccity.R;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by archi1 on 11/25/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context mContext;
    private List<GalleryDetails> galleryDetailsList;
    private String pos_canvas,strTitle_canvas,strImage_canvas,strCanvasImg_canvas,strSizePrice_canvas,transactionId;
    public PayPalPayment payment;
    public Double amountGalleryImage;
    private Bitmap image;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.CLIENT_ID);


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);
        amountGalleryImage = 3.00;
    //    amountPayStr = 15.00;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        final GalleryDetails album = galleryDetailsList.get(position);
        final String pos = album.getId();
        final String strTitle = album.getName();
        final String strImage = album.getImage();
        final String strCanvasImg = album.getCanvas_image();
        final String strSizePrice = album.getSize();

        holder.glryImageTitle.setText(album.getName());

        Glide.with(mContext).load(album.getCanvas_image()).placeholder(R.drawable.ic_placeholder).into(holder.galleryImage);
        Log.d("CANVAS_IMAGE",""+album.getCanvas_image());
        Log.d("IMAGE",""+album.getImage());

        holder.galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GalleryDetailsActivity.class);
                Gson gson=new Gson();
                i.putExtra("canvaspicdetails",gson.toJson(album));
                mContext.startActivity(i);
            }
        });
    }



    private void showPopupMenu(ImageView menuImageView) {

        PopupMenu popupMenu = new PopupMenu(mContext, menuImageView);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_album, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }


    @Override
    public int getItemCount() {
        return galleryDetailsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryImage;
        TextView glryImageTitle;
        //ImageView menuImageView;

        MyViewHolder(View view) {
            super(view);
            galleryImage = (ImageView) view.findViewById(R.id.adapter_gallery_image);
            glryImageTitle = (TextView) view.findViewById(R.id.adapter_gallery_image_title);
            //menuImageView = (ImageView) view.findViewById(R.id.overflow);
            galleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public GalleryAdapter(Context mContext, List<GalleryDetails> galleryDetailsList) {
        this.mContext = mContext;
        this.galleryDetailsList = galleryDetailsList;
    }

    public void onBuyPressed() {

        payment = new PayPalPayment(new BigDecimal(amountGalleryImage), "USD", "Pic Citi",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(mContext, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        ((Activity) mContext).startActivityForResult(intent, 0);
    }



    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.adapter_gallery_image_send_my_gallery:
                onBuyPressed();
                    Toast.makeText(mContext, "send my gallery", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.adapter_gallery_image_canvas_gallery_pic:
                    Intent put_canvas_detail = new Intent(mContext,GalleryDetailsActivity.class);
                    put_canvas_detail.putExtra("id",pos_canvas);
                    put_canvas_detail.putExtra("title",strTitle_canvas);
                    put_canvas_detail.putExtra("image",strImage_canvas);
                    put_canvas_detail.putExtra("canvasImage",strCanvasImg_canvas);
                    put_canvas_detail.putExtra("sizePrice",strSizePrice_canvas);
                    mContext.startActivity(put_canvas_detail);
                    Toast.makeText(mContext, "canvas gallery  ", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.adapter_gallery_image_paste_me:
                    return true;
                default:
            }
            return false;
        }
    }


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
                    transactionId = jsonObject.getJSONObject("response").getString("id");
                    Log.d("user_id", Utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId));
                    Log.d("transactionId", transactionId);
                    Log.d("imgid", pos_canvas);
                    Log.d("imgprice", strSizePrice_canvas);
                    Log.d("img", strCanvasImg_canvas);

                    //new MassPayment(String.valueOf(amountGalleryImage),"jagdis_1286435646_per@gmail.com");

                    new AddPayamentInfo(transactionId).execute();

                    if (main.getString("state").equalsIgnoreCase("approved")) {
                        File direct = new File(Environment.getExternalStorageDirectory() + "/Camera");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }


                        /*DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(imgOriginal);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        String name = Environment.getExternalStorageDirectory().getAbsolutePath();*//*

                        name += "/piccity/";
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false).setTitle("Piccity")
                                .setDescription("Something Usefull")
                                .setDestinationInExternalPublicDir(name, "picicty.jpg");
                        mgr.enqueue(request);*/

                        Log.d("PaymentRespond", Respond);
                        Toast.makeText(mContext, "Payment Succesfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            user_id =  Utils.ReadSharePrefrence(mContext,Constant.USER_ID);
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            hashMap.put("user_id",user_id);
            hashMap.put("imgid",pos_canvas);
            hashMap.put("imgprice",strSizePrice_canvas);
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
            Toast.makeText(mContext, "fgh"+user_id, Toast.LENGTH_SHORT).show();
            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    //Toast.makeText(GalleryDetailsActivity.this, "Successffully Inserted...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, "Error"+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
