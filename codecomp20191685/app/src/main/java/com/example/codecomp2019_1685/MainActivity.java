package com.example.codecomp2019_1685;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = MainActivity.class.getName();

    // Instances
    private GoogleAPIWrapper googleAPI = new GoogleAPIWrapper();

    // Constructors
    public MainActivity() {
        Log.i(TAG, "Initializing Main Activity.");
    }

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button overlayButton = (Button) findViewById(R.id.button);
        final Button ttsButton = (Button) findViewById(R.id.TTS);
        final Button camera;
        overlayButton.setOnClickListener(new View.OnClickListener() {
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
        });
        // TODO: Use for print screens
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final InputStream inputStream = getResources().openRawResource(R.raw.shrekisloveshrekislife);
                    final byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();

                    final Image inputImage = new Image();
                    inputImage.encodeContent(photoData);

                    // Text detections detect the text on the image.
                    final String textDetect = googleAPI.computeTextDetection(inputImage);
                    // Web detections detect the subject on the image.
                    final String webDetect = googleAPI.computeWebDetection(inputImage);

                    Log.i(TAG, "Returned Shit: \nText:\n" + textDetect + "\nWeb Type:\n" + webDetect);
                }
                catch (IOException ex) {
                    // TODO: Put log error into.
                    Log.e(TAG, "IO failure at" + ex.getStackTrace());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
