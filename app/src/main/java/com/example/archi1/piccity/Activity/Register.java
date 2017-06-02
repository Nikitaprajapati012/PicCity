package com.example.archi1.piccity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    public Utils utils;
    public EditText registername, registermobile, registerEmail, registerPAssword, registerPaypalEmail;
    public Button register;
    public CircleImageView userImage;
    public String strName;
    public String strMobile;
    public String strEmail;
    public String strPassword, strPaypalEmail;
    public Bitmap bitmap;
    public String strUserProfile = "";
    private String DEVICE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        utils = new Utils(Register.this);
        registername = (EditText) findViewById(R.id.edt_name);
        registermobile = (EditText) findViewById(R.id.edt_mobile);
        registerEmail = (EditText) findViewById(R.id.edt_email);
        registerPAssword = (EditText) findViewById(R.id.edt_pswd);
        registerPaypalEmail = (EditText) findViewById(R.id.edt_paypalemail);
        userImage = (CircleImageView) findViewById(R.id.img_register_user);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        register = (Button) findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        DEVICE_ID = FirebaseInstanceId.getInstance().getToken();
        Utils.WriteSharePrefrence(Register.this, Constant.DEVICE_ID, DEVICE_ID);
        Toast.makeText(this, "DEVICE_ID" + DEVICE_ID, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                strUserProfile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkValidation() {
        strName=registername.getText().toString().trim();
        strMobile=registermobile.getText().toString().trim();
        strEmail=registerEmail.getText().toString().trim();
        strPassword=registerPAssword.getText().toString().trim();
        strPaypalEmail=registerPaypalEmail.getText().toString().trim();

        if (registername.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter name", Toast.LENGTH_SHORT).show();
        } else if (registermobile.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {
            Toast.makeText(Register.this, "please Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else if (registerPAssword.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter Pasword", Toast.LENGTH_SHORT).show();
        } else {
            new signup().execute();
        }
    }


    private class signup extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Register.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            //String Url = Constant.Base_URL+"register.php?email="+strEmail+"&name="+strName+"&password="+strPassword+"&phone="+strMobile+"&image="+strUserProfile;
            hashMap.put("email",strEmail);
            hashMap.put("name", strName);
            hashMap.put("password", strPassword);
            hashMap.put("phone", strMobile);
            hashMap.put("deviceid", DEVICE_ID);
            hashMap.put("paypal_email", strPaypalEmail);
            Log.d("pay",""+strPaypalEmail);

            if (strUserProfile.equalsIgnoreCase("")) {
                Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.splashscreen);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapOrg.compress(Bitmap.CompressFormat.PNG, 100, bao);
                byte[] ba = bao.toByteArray();
                String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                hashMap.put("image", ba1);
                Log.e("PNG", ba1);
                //new signup(hashMap).execute();
            } else {
                hashMap.put("image", strUserProfile);
                // new signup(hashMap).execute();
                Log.e("PNG", strUserProfile);
            }
            //http://web-medico.com/web1/pic_citi/Api/registration.php?email=nikita1@gmail.com&name=nikita&phone=9785211&deviceid=989763963&password=nikita1&paypal_email=niki1@gmail.com&image=1aa234364fc661520a4cec9ea611657d.jpg
            return Utils.getResponseofPost(Constant.Base_URL + "registration.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE", s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(Register.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Register.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}