package com.example.archi1.piccity.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Model.SizePrice;
import com.example.archi1.piccity.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CanvasGalleryImage extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    public Utils utils;
    public ArrayList<SizePrice> arraylistSizePrize;
    public String strCanvasimgId, strCanvasImgTitle, strCanvasImgOriginal, strCanvasImgCanvas, strCanvasSizePrice;
    public ImageView ivcanvasGalleryImage, header_iv_back;
    public Spinner spinnerCanvasGalleryImageSize;
    public TextView txtCanvasGalleryImagePrice,txtCanvasGalleryImagetitle;
    public Button btnCanvasImage,btnSubmit, btnCancel;
    public EditText edAddress,edDescription,edQuantity,edZipCode;
    public Spinner spinnerCountry,spinnerState,spinnerCity;
    public String strAddress,strDescription,strQuantity,strZipCode,strCountryId,strStateId,strCityId;
    public Dialog dialog;
    public ArrayList<String> arrayCountryNameList,arrayCountryIdList,arrayStateNameList,arrayStateIdList,arrayCityNameList,arrayCityIdList;
    public ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_gallery_image);
        utils = new Utils(CanvasGalleryImage.this);
        arraylistSizePrize = new ArrayList<>();
        init();
        click();
    }

    // TODO: 5/1/2017 perform click event
    private void click() {
        header_iv_back.setOnClickListener(this);
        btnCanvasImage.setOnClickListener(this);
    }

    private void init() {
        btnCanvasImage = (Button) findViewById(R.id.btn_canvas_gallery_image);
        ivcanvasGalleryImage = (ImageView) findViewById(R.id.iv_canvas_gallery_image);
        spinnerCanvasGalleryImageSize = (Spinner) findViewById(R.id.spinner_canvas_gallery_image_size);
        txtCanvasGalleryImagePrice = (TextView) findViewById(R.id.txt_canvas_gallery_image_price);
        txtCanvasGalleryImagetitle = (TextView) findViewById(R.id.txt_canvas_gallery_img_title);
        header_iv_back = (ImageView) findViewById(R.id.header_iv_back);
        strCanvasSizePrice = utils.ReadSharePrefrence(getApplicationContext(), Constant.CanvasSizePrice);

        if (getIntent().getExtras() != null) {
            strCanvasimgId = getIntent().getExtras().getString("canvasID");
            strCanvasImgTitle = getIntent().getExtras().getString("canvasImageTitle");
            strCanvasImgOriginal = getIntent().getExtras().getString("canvasImageOriginal");
            strCanvasImgCanvas = getIntent().getExtras().getString("canvasImageCanvasType");
            Log.d("strCanvasImgOriginal",""+strCanvasImgOriginal);
            txtCanvasGalleryImagetitle.setText("Uploader Name : "+strCanvasImgTitle);
            Glide.with(getApplicationContext()).load(strCanvasImgOriginal).placeholder(R.drawable.ic_placeholder).into(ivcanvasGalleryImage);

            try {
                JSONArray jsonArray = new JSONArray(strCanvasSizePrice);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    SizePrice CanvassizePrice = new SizePrice();
                    String canvasImgId = jsonObject.getString("id");
                    String canvasImgPrice = jsonObject.getString("price");
                    String canvasImgSize = jsonObject.getString("size");

                    CanvassizePrice.setId(canvasImgId);
                    CanvassizePrice.setPrice(canvasImgPrice);
                    CanvassizePrice.setSize(canvasImgSize);
                    arraylistSizePrize.add(CanvassizePrice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> CanvasImageSize = new ArrayList<>();
            for (int i = 0; i < arraylistSizePrize.size(); i++) {
                CanvasImageSize.add(arraylistSizePrize.get(i).getSize());
            }

            ArrayAdapter<String> spinnerSizePriceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CanvasImageSize);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_canvas_gallery_image:
                dialog = new Dialog(CanvasGalleryImage.this);
                dialog.setContentView(R.layout.dialog_image_canvas);
                dialog.setTitle("");
                edAddress = (EditText) dialog.findViewById(R.id.dialog_edaddress);
                edDescription = (EditText) dialog.findViewById(R.id.dialog_eddescription);
                edQuantity = (EditText) dialog.findViewById(R.id.dialog_edquantity);
                edZipCode = (EditText) dialog.findViewById(R.id.dialog_edzipcode);
                btnSubmit = (Button) dialog.findViewById(R.id.dialog_btnsubmit);
                btnCancel = (Button) dialog.findViewById(R.id.dialog_btncancel);
                spinnerCountry = (Spinner) dialog.findViewById(R.id.dialog_spinnercountry);
                spinnerState = (Spinner) dialog.findViewById(R.id.dialog_spinnerstate);
                spinnerCity = (Spinner) dialog.findViewById(R.id.dialog_spinnercity);
                strAddress=edAddress.getText().toString();
                strDescription=edDescription.getText().toString();
                strQuantity=edQuantity.getText().toString();
                strZipCode=edZipCode.getText().toString();
                spinnerCountry.setOnItemSelectedListener(this);
                spinnerState.setOnItemSelectedListener(this);
                spinnerCity.setOnItemSelectedListener(this);
                new GetCountryDetails().execute();
                dialog.show();
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.header_iv_back:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int sel;
        switch (adapterView.getId()){
            case R.id.dialog_spinnercountry:
                if (position > 0) {
                    sel = (int) spinnerCountry.getItemIdAtPosition(position);
                    strCountryId = arrayCountryIdList.get(sel);
                } else {
                    sel = (int) spinnerCountry.getItemIdAtPosition(position);
                    strCountryId = arrayCountryIdList.get(sel);
                }
                new GetStatesDetails(strCountryId).execute();
                break;
            case R.id.dialog_spinnerstate:
                if (position > 0) {
                    sel = (int) spinnerState.getItemIdAtPosition(position);
                    strStateId = arrayStateIdList.get(sel);
                } else {
                    sel = (int) spinnerState.getItemIdAtPosition(position);
                    strStateId = arrayStateIdList.get(sel);
                }
                new GetCityDetails(strStateId).execute();
                break;

            case R.id.dialog_spinnercity:
                if (position > 0) {
                    sel = (int) spinnerCity.getItemIdAtPosition(position);
                    strCityId = arrayCityIdList.get(sel);
                } else {
                    sel = (int) spinnerCity.getItemIdAtPosition(position);
                    strCityId = arrayCityIdList.get(sel);
                }
                break;
        }
         }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    // TODO: 5/1/2017 get the details of country
    private class GetCountryDetails extends AsyncTask<String,String ,String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CanvasGalleryImage.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/get_country_list.php
            return Utils.getResponseofGet(Constant.Base_URL + "get_country_list.php");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE",""+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                arrayCountryIdList = new ArrayList<>();
                arrayCountryNameList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        arrayCountryIdList.add(object.getString("id"));
                        arrayCountryNameList.add(object.getString("country_name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(arrayCountryNameList.size() > 0){
                openCountryList();
            }
            else{
                Toast.makeText(CanvasGalleryImage.this, "Data not Get", Toast.LENGTH_SHORT).show();
            }
        }
    }

      private void openCountryList() {
        // TODO: 4/19/2017 set adapter for show the the country in spinner
        spinnerAdapter = new ArrayAdapter<String>(CanvasGalleryImage.this, android.R.layout.simple_spinner_item, arrayCountryNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(spinnerAdapter);
    }

    // TODO: 5/1/2017 get the details of states as per country select.
    private class GetStatesDetails extends AsyncTask<String,String ,String> {
        ProgressDialog pd;
        String countryId;

        public GetStatesDetails(String strCountryId) {
            this.countryId=strCountryId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CanvasGalleryImage.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/get_states_list.php?country_id=1
            return Utils.getResponseofGet(Constant.Base_URL + "get_states_list.php?country_id=" +countryId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE states list",""+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                arrayStateIdList = new ArrayList<>();
                arrayStateNameList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        arrayStateIdList.add(object.getString("id"));
                        arrayStateNameList.add(object.getString("region_name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(arrayStateNameList.size() > 0){
                openStateList();
            }
        }
    }

    private void openStateList() {
        // TODO: 4/19/2017 set adapter for show the the state in spinner
        spinnerAdapter = new ArrayAdapter<String>(CanvasGalleryImage.this, android.R.layout.simple_spinner_item, arrayStateNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(spinnerAdapter);
    }
    // TODO: 5/1/2017 get the details of city as per state select.
    private class GetCityDetails extends AsyncTask<String,String ,String> {
        ProgressDialog pd;
        String stateId;

        public GetCityDetails(String strStateId) {
            this.stateId=strStateId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CanvasGalleryImage.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web1/pic_citi/Api/get_city_list.php?state_id=42
            return Utils.getResponseofGet(Constant.Base_URL + "get_city_list.php?state_id=" +stateId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("RESPONSE city list",""+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                arrayCityIdList = new ArrayList<>();
                arrayCityNameList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("true")){
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        arrayCityIdList.add(object.getString("id"));
                        arrayCityNameList.add(object.getString("city_name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(arrayCityNameList.size() > 0){
                openCityList();
            }
        }
    }

    private void openCityList() {
        // TODO: 4/19/2017 set adapter for show the the city in spinner
        spinnerAdapter = new ArrayAdapter<String>(CanvasGalleryImage.this, android.R.layout.simple_spinner_item, arrayCityNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(spinnerAdapter);
    }
}
