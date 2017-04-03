package com.example.archi1.piccity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public Button btnLogin;
    public EditText etEmailLogin, etPasseordLogin;
    public String strEmail, strPassword;
    public Utils utils;
    public TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        utils = new Utils(LoginActivity.this);

        etEmailLogin=(EditText)findViewById(R.id.activity_login_email);
        etPasseordLogin=(EditText)findViewById(R.id.activity_login_password);
        tvSignUp=(TextView)findViewById(R.id.activity_login_signup);
        btnLogin=(Button)findViewById(R.id.activity_login_submit);

        init();
    }

    private void init() {
        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.activity_login_submit:

                strEmail = etEmailLogin.getText().toString().trim();
                strPassword=etPasseordLogin.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
                    if (strPassword.length()<8){
                        Toast.makeText(LoginActivity.this, "Please Enter eight digits password", Toast.LENGTH_SHORT).show();

                    }else {
                        if (utils.isConnectingToInternet()){
                            new gettLogin().execute();

                        }else {
                            Toast.makeText(LoginActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email ID", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.activity_login_signup :

                Intent intent = new Intent(LoginActivity.this,Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

        }


    }


    private class gettLogin extends AsyncTask<String,String,String>{

        public ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // String URL = "http://web-medico.com/web1/pic_citi/Api/login.php?email=" + email + "&password=" + password;
            String Url = Constant.Base_URL+"login.php?email="+ strEmail +"&password=" +strPassword;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("loginData",s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status")==true){

                    JSONObject object = jsonObject.getJSONObject("data");
                    int strUserId = object.getInt("id");
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.UserId,String.valueOf(strUserId));
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.Name,object.getString("name"));
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.Email,object.getString("email"));
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.Mobile,object.getString("phone"));
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.Image,object.getString("image"));
                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.USER_PASS,object.getString("password"));
                    Log.d("id", String.valueOf(object.getInt("id")));
                    Log.d("user",object.getString("password"));

                    Utils.WriteSharePrefrence(LoginActivity.this,Constant.USER_ID,String.valueOf(object.getInt("id")));
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pd.dismiss();
            /*{"status":"true","msg":"login successfull","data":{"id":"49","name":"gopu","email":"gopu@gmail.com","phone":"1234567890","image":"http:\/\/web-medico.com\/web1\/pic_citi\/Api\/images\/100_600124391.jpg"}}*/
            Log.d("Rujul",s);
        }
    }
}