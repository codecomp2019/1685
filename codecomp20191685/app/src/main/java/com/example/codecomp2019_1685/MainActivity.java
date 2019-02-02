package com.example.codecomp2019_1685;

import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
    // Constants
    private static final String TAG = MainActivity.class.getName();

    // Constructors
    private static final String TAG = "MainActivity";

    public MainActivity() {

    // Methods        Log.i(TAG, "Initializing.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button overlayButton = (Button) findViewById(R.id.button);
        Button camera;
        overlayButton.setOnClickListener(new View.OnClickListener()
        
        {
            @Override
            public void onClick(View view)
            {
                if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(MainActivity.this)) {   //Android M Or Over
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 2038);
                    return;
                }
                startService(new Intent(MainActivity.this, ButtonOverlay.class));
            }    
            
        }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
