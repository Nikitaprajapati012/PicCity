package com.example.archi1.piccity.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


public class MyFontTextViewTitle extends TextView {

    private static final String TAG = "TextView";

    private Typeface typeface;

    public MyFontTextViewTitle(Context context) {
        super(context);
    }

    public MyFontTextViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, "");
    }

    public MyFontTextViewTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, "");
    }

//	private void setCustomFont(Context ctx, AttributeSet attrs) {
//		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
//		String customFont = a.getString(R.styleable.app_customFont);
//		setCustomFont(ctx, customFont);
//		a.recycle();
//	}

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "fonts/Calibri.ttf");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }
        setTypeface(typeface);
        return true;
    }

}