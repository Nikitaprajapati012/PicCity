package com.example.archi1.piccity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class PicCity extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }
}