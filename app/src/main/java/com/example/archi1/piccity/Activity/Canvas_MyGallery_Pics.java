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

public class Canvas_MyGallery_Pics extends AppCompatActivity {

    public Utils utils;

    public ArrayList<SizePrice> arraylistSizePrize;
    public String strCanvasimgId, strCanvasImgTitle, strCanvasImgCanvas, strCanvasSizePrice;
    public ImageView ivcanvasGalleryImage, header_iv_back;
    public Spinner spinnerCanvasGalleryImageSize;
    public TextView txtCanvasGalleryImagePrice;
    public TextView txtCanvasGalleryImagetitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas__my_gallery__pics2);

        utils = new Utils(Canvas_MyGallery_Pics.this);
        arraylistSizePrize = new ArrayList<>();

        init();
        header_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void init() {
        ivcanvasGalleryImage = (ImageView) findViewById(R.id.iv_canvas_mygallery_image);
        spinnerCanvasGalleryImageSize = (Spinner) findViewById(R.id.spinner_canvas_mygallery_image_size);
        txtCanvasGalleryImagePrice = (TextView) findViewById(R.id.txt_canvas_mygallery_image_price);
        txtCanvasGalleryImagetitle = (TextView) findViewById(R.id.txt_canvas_mygallery_img_title);
        header_iv_back = (ImageView) findViewById(R.id.header_iv_back);


            strCanvasimgId = getIntent().getStringExtra("Id");
            strCanvasImgCanvas = getIntent().getStringExtra("Image");
            strCanvasImgTitle = getIntent().getStringExtra("ImageName");
        Log.d("strCanvasimgId",""+strCanvasimgId);
        Log.d("strCanvasImgCanvas",""+strCanvasImgCanvas);
        Log.d("strCanvasImgTitle",""+strCanvasImgTitle);
        strCanvasSizePrice = utils.ReadSharePrefrence(getApplicationContext(), Constant.CanvasSizePrice);

        Log.d("strCanvasSizePrice",""+strCanvasSizePrice);
            txtCanvasGalleryImagetitle.setText(strCanvasImgTitle);
            Glide.with(getApplicationContext()).load(strCanvasImgCanvas).placeholder(R.drawable.ic_placeholder).into(ivcanvasGalleryImage);

            try {
                JSONArray jsonArray = new JSONArray(strCanvasSizePrice.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    SizePrice CanvassizePrice = new SizePrice();
                    String canvasImgId = jsonObject.getString("id");
                    String canvasImgPrice = jsonObject.getString("price");
                    String canvasImgSize = jsonObject.getString("size");

                    CanvassizePrice.setId(canvasImgId);
                    CanvassizePrice.setPrice(canvasImgPrice);
                    CanvassizePrice.setSize(canvasImgSize);

                    Log.d("SJAS",""+jsonObject.getString("id")+jsonObject.getString("price")+ jsonObject.getString("size"));
                    Log.d("vijay","id :"+canvasImgId + "\n price :"+canvasImgPrice+ "\n "+canvasImgSize +"\n ==============");
                    arraylistSizePrize.add(CanvassizePrice);
                }
                //Log.d("vijay","array size :"+arraylistSizePrize.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> CanvasImageSize = new ArrayList<>();
            for (int i = 0; i < arraylistSizePrize.size(); i++) {
                CanvasImageSize.add(arraylistSizePrize.get(i).getSize());

            }

            ArrayAdapter<String> adpatersetsize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CanvasImageSize);
            adpatersetsize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCanvasGalleryImageSize.setAdapter(adpatersetsize);

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

