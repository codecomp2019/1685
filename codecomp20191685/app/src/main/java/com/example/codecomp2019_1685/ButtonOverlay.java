package com.example.codecomp2019_1685;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class ButtonOverlay extends Service
{
    // Constants
    private static final String TAG = ButtonOverlay.class.getName();

    // Instances
    private WindowManager windowManager;
    private ImageView button;

    // Constructors
    public ButtonOverlay() {
        Log.i(TAG, "Initializing.");
    }

    // Methods
   @Override
   public void onCreate()
   {
        super.onCreate();
        Log.i(TAG, "I have reched creation of a window.");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        button = new ImageView(this);
        button.setImageResource(R.drawable.circle);
        int LAYOUT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } 
        else
        {
            LAYOUT= WindowManager.LayoutParams.TYPE_PHONE;

        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,

                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(button, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (button != null) windowManager.removeView(button);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}