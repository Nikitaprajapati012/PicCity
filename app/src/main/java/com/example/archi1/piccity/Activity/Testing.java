package com.example.archi1.piccity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

/**
 * Created by archi1 on 12/15/2016.
 */

public class Testing extends AppCompatActivity implements View.OnClickListener {

    public Utils utils;
    public ImageView productImageView;
    public TextView tvProductName;
    public TextView tvProductDesc;
    public TextView tvProductPrice;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        utils = new Utils(Testing.this);
        productImageView=(ImageView)findViewById(R.id.activity_product_detail_product_image_pager);
        tvProductName=(TextView)findViewById(R.id.activity_product_detail_product_name);
        tvProductDesc=(TextView)findViewById(R.id.activity_product_details_product_discription);
        tvProductPrice=(TextView)findViewById(R.id.activity_product_detail_product_price);
        init();
    }

    private void init() {

       String imgID = getIntent().getExtras().getString("id");
       String imgDescription = getIntent().getExtras().getString("description");
       String imgPath = getIntent().getExtras().getString("image");
       String imgPrice = getIntent().getExtras().getString("price");
      String  name = getIntent().getExtras().getString("name");
       String imgLocation = getIntent().getExtras().getString("location");
       String username = getIntent().getExtras().getString("username");
        String userid = getIntent().getExtras().getString("userid");

        String currency = getIntent().getExtras().getString("currency");


        tvProductName.setText(name);
        tvProductPrice.setText(imgPrice);
        tvProductDesc.setText(imgDescription);
        Glide.with(getApplicationContext()).load(imgPath).placeholder(R.drawable.ic_placeholder).into(productImageView);



    }

    @Override
    public void onClick(View v) {

    }
}
