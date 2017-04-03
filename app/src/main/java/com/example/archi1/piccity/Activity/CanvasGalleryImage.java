package com.example.archi1.piccity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.SizePrice;
import com.example.archi1.piccity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CanvasGalleryImage extends AppCompatActivity {

    public Utils utils;

    public ArrayList<SizePrice> arraylistSizePrize;
    public String strCanvasimgId,strCanvasImgTitle,strCanvasImgOriginal,strCanvasImgCanvas,strCanvasSizePrice;
    public ImageView ivcanvasGalleryImage;
    public Spinner spinnerCanvasGalleryImageSize;
    public TextView txtCanvasGalleryImagePrice;
    public TextView txtCanvasGalleryImagetitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_gallery_image);

        utils= new Utils(CanvasGalleryImage.this);
        arraylistSizePrize = new ArrayList<>();

        init();
    }


    private void init() {

        ivcanvasGalleryImage=(ImageView)findViewById(R.id.iv_canvas_gallery_image);
        spinnerCanvasGalleryImageSize=(Spinner)findViewById(R.id.spinner_canvas_gallery_image_size);
        txtCanvasGalleryImagePrice=(TextView)findViewById(R.id.txt_canvas_gallery_image_price);
        txtCanvasGalleryImagetitle=(TextView)findViewById(R.id.txt_canvas_gallery_img_title);

        if (getIntent().getExtras() != null){
            strCanvasimgId = getIntent().getExtras().getString("canvasID");
            strCanvasImgTitle=getIntent().getExtras().getString("canvasImageTitle");
            strCanvasImgOriginal=getIntent().getExtras().getString("canvasImageOriginal");
            strCanvasImgCanvas=getIntent().getExtras().getString("canvasImageCanvasType");
            strCanvasSizePrice=getIntent().getExtras().getString("canvasImageSizeAndPrice");


            txtCanvasGalleryImagetitle.setText(strCanvasImgTitle);
            Glide.with(getApplicationContext()).load(strCanvasImgOriginal).placeholder(R.drawable.ic_placeholder).into(ivcanvasGalleryImage);

            try {
                JSONArray jsonArray = new JSONArray(strCanvasSizePrice.toString());
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    SizePrice CanvassizePrice = new SizePrice();
                    String canvasImgId = jsonObject.getString("id");
                    String canvasImgPrice = jsonObject.getString("price");
                    String canvasImgSize = jsonObject.getString("size");

                    CanvassizePrice.setId(canvasImgId);
                    CanvassizePrice.setPrice(canvasImgPrice);
                    CanvassizePrice.setSize(canvasImgSize);
                    //Log.d("vijay","id :"+id + "\n price :"+price+ "\n "+size +"\n ==============");
                    arraylistSizePrize.add(CanvassizePrice);
                }
                //Log.d("vijay","array size :"+arraylistSizePrize.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> CanvasImageSize = new ArrayList<>();
            for (int i = 0; i<arraylistSizePrize.size();i++){
                CanvasImageSize.add(arraylistSizePrize.get(i).getSize());
            }

             ArrayAdapter<String> spinnerSizePriceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,CanvasImageSize);
            spinnerSizePriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCanvasGalleryImageSize.setAdapter(spinnerSizePriceAdapter);

            spinnerCanvasGalleryImageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String CanvasImagePrice = arraylistSizePrize.get(position).getPrice();

                    txtCanvasGalleryImagePrice.setText(CanvasImagePrice);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    }


}
