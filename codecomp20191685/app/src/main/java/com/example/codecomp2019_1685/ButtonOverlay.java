package com.example.codecomp2019_1685;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class ButtonOverlay extends Service
{
    // Constants
    private static final String TAG = ButtonOverlay.class.getName();

    // Instances
    private WindowManager windowManager;
    WindowManager.LayoutParams params;
    private ImageView button;
    private Button buttons;

    // Constructors
    public ButtonOverlay() {
        Log.i(TAG, "Initializing.");
    }

    /*@Override
    public boolean performClick() {
        button.performClick();

        return true;
    }*/
    // Methods
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG, "I have reached creation of a window.");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // button = new ImageView(this);
        buttons = new Button(this);
        // button.setImageResource(R.drawable.circle);
        int LAYOUT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else
        {
            LAYOUT= WindowManager.LayoutParams.TYPE_PHONE;

        }
        params = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,

                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        windowManager.addView(buttons, params);

        buttons.setOnTouchListener(new View.OnTouchListener(){


            @Override public boolean onTouch (View v, MotionEvent event)
            {
                switch (event.getAction())
                {

                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) event.getRawX() - 150;
                        params.y =  (int) event.getRawY() -150;
                        windowManager.updateViewLayout(buttons,params);
                        return true;


                }
                return false;
            }

        });
        buttons.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Intent screenshotIntent = new Intent(ButtonOverlay.this, ScreenshotCommand.class);
                screenshotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(screenshotIntent);
                return true;
            }
        });
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