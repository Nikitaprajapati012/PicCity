package com.example.archi1.piccity.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.Fragment.GalleryFragment;
import com.example.archi1.piccity.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {

        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.ReadSharePrefrence(SplashActivity.this, Constant.UserId).equals("")){
                    Intent in = new Intent(SplashActivity.this,LoginActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }else {
                    Toast.makeText(SplashActivity.this, "sucessss", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SplashActivity.this,MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }


            }
        },2000);
    }
}
