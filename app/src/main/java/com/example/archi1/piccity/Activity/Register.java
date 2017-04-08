package com.example.archi1.piccity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    public Utils utils;
    public EditText registername, registermobile, registerEmail, registerPAssword;
    public Button register;
    public ImageView userImage;
    public String strName;
    public String strMobile;
    public String strEmail;
    public String strPassword;
    public int strCheck = 0;
    public Bitmap bitmap;
    public String strUserProfile = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        utils = new Utils(Register.this);

        registername = (EditText) findViewById(R.id.edt_name);
        registermobile = (EditText) findViewById(R.id.edt_mobile);
        registerEmail = (EditText) findViewById(R.id.edt_email);
        registerPAssword = (EditText) findViewById(R.id.edt_pswd);

        userImage = (ImageView) findViewById(R.id.img_register_user);

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

        if (registername.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter name", Toast.LENGTH_SHORT).show();
        } else if (registermobile.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {
            Toast.makeText(Register.this, "please Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else if (registerPAssword.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please Enter Pasword", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, String> hashMap = new HashMap<>();
            //String Url = Constant.Base_URL+"register.php?email="+strEmail+"&name="+strName+"&password="+strPassword+"&phone="+strMobile+"&image="+strUserProfile;
            hashMap.put("email", strEmail);
            hashMap.put("name", strName);
            hashMap.put("password", strPassword);
            hashMap.put("phone", strMobile);
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
            }
            new signup(hashMap).execute();

        }
    }


    private class signup extends AsyncTask<String, String, String> {


        ProgressDialog pd;
        HashMap<String, String> hashMap;

        public signup(HashMap<String, String> hashMap) {
            this.hashMap = hashMap;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strName = registername.getText().toString();
            strEmail = registerEmail.getText().toString();
            strMobile = registermobile.getText().toString();
            strPassword = registerPAssword.getText().toString();
            pd = new ProgressDialog(Register.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //"http://web-medico.com/web1/pic_citi/Api/register.php?email=" + email + "&name=" + name + "&password=" + password + "&phone=" + mobile + "&image=" + userImage;

            return utils.getResponseofPost(Constant.Base_URL + "register.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            /*{"successful":"true","msg":"registration sucessful","id":39}*/
            super.onPostExecute(s);
            Log.d("gopal", s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("successful").equalsIgnoreCase("true")) {
                    String login_id = object.getString("id");
                    Toast.makeText(Register.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                  /*  ChatUtils chatUtils = new ChatUtils(Register.this);
                    chatUtils.ChatRegister(Register.this, login_id,strName, strEmail, strPassword);
                    Toast.makeText(Register.this, ""+login_id, Toast.LENGTH_SHORT).show();
                   */
                    Intent i = new Intent(Register.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Register.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("gopal", s);
            pd.dismiss();
        }
    }
}