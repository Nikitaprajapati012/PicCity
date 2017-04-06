package com.example.archi1.piccity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CanvasRoyaltyImage extends AppCompatActivity {

    public Utils utils;
    public ArrayList<SizePrice> arraylistSizePrize;
    public String strCanvasRoyaltyimgId, strCanvasRoyaltyImgUserName, strCanvasRoyaltyUserId, strCanvasRoyaltyImg;
    public ImageView ivcanvasRoyaltyImage,header_iv_back;
    public Spinner spinnerCanvasRoyaltyImageSize;
    public TextView txtCanvasRoyaltyImagePrice;
    public TextView txtCanvasRoyaltyImageOwnerName;
    public String RoyaltyCanvasSizePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_royalty_image);

        utils = new Utils(CanvasRoyaltyImage.this);
        RoyaltyCanvasSizePrice = utils.ReadSharePrefrence(getApplicationContext(), Constant.CanvasSizePrice);
        arraylistSizePrize = new ArrayList<>();

        init();

        header_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {

        ivcanvasRoyaltyImage = (ImageView) findViewById(R.id.iv_canvas_royalty_image);
        spinnerCanvasRoyaltyImageSize = (Spinner) findViewById(R.id.spinner_canvas_royalty_image_size);
        txtCanvasRoyaltyImagePrice = (TextView) findViewById(R.id.txt_canvas_royalty_image_price);
        txtCanvasRoyaltyImageOwnerName = (TextView) findViewById(R.id.txt_canvas_royalty_img_owner_name);
        header_iv_back = (ImageView)findViewById(R.id.header_iv_back);
        if (getIntent().getExtras() != null) {

            strCanvasRoyaltyimgId = getIntent().getExtras().getString("canvasRoyaltyImageId");
            strCanvasRoyaltyUserId = getIntent().getExtras().getString("canvasRoyaltyUserId");
            strCanvasRoyaltyImgUserName = getIntent().getExtras().getString("canvasRoyaltyUserName");
            strCanvasRoyaltyImg = getIntent().getExtras().getString("canvasRoyaltyImage");

            txtCanvasRoyaltyImageOwnerName.setText(strCanvasRoyaltyImgUserName);
            Glide.with(getApplicationContext()).load(strCanvasRoyaltyImg).placeholder(R.drawable.ic_placeholder).into(ivcanvasRoyaltyImage);


            try {
                JSONArray jsonArray = new JSONArray(RoyaltyCanvasSizePrice.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    SizePrice sizePrice = new SizePrice();
                    String canvasRoyaltyImgId = object.getString("id");
                    String canvasRoyaltyImgPrice = object.getString("price");
                    String canvasRoyaltyImgSize = object.getString("size");

                    sizePrice.setId(canvasRoyaltyImgId);
                    sizePrice.setPrice(canvasRoyaltyImgPrice);
                    sizePrice.setSize(canvasRoyaltyImgSize);

                    arraylistSizePrize.add(sizePrice);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<String> CanvasRoyaltyImageSize = new ArrayList<>();
            for (int i = 0; i < arraylistSizePrize.size(); i++) {
                CanvasRoyaltyImageSize.add(arraylistSizePrize.get(i).getSize());
                ArrayAdapter<String> spinnerRoyaltySizePriceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CanvasRoyaltyImageSize);
                spinnerRoyaltySizePriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCanvasRoyaltyImageSize.setAdapter(spinnerRoyaltySizePriceAdapter);


                spinnerCanvasRoyaltyImageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String CanvasRoyaltyImageprice = arraylistSizePrize.get(position).getPrice();

                        txtCanvasRoyaltyImagePrice.setText(CanvasRoyaltyImageprice);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }
    }
}
