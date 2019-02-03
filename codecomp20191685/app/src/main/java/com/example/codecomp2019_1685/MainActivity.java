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

    // Inner Class
    private class GoogleRunnable implements Runnable {
        // Instances
        private Image inputImage;
        private String textDetect;
        private String webDetect;

        // Constructors
        private GoogleRunnable() { }

        public GoogleRunnable(Image inputImage) {
            this();
            this.inputImage = inputImage;
        }

        // Methods
        @Override
        public void run() {
            try {
                // Text detections detect the text on the image.
                this.textDetect = googleAPI.computeTextDetection(this.inputImage);
                // Web detections detect the subject on the image.
                this.webDetect = googleAPI.computeWebDetection(this.inputImage);
            } catch (IOException ex) {
                // TODO: Put log error into.
                Log.e(TAG, "IO failure at" + ex.getStackTrace());
            }
            return;
        }
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
        // TODO: Use for print
        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TTS", "TTS has been Pressed");

                final Image inputImage = new Image();
                final int location = R.raw.laundry;

                try {
                    final InputStream inputStream = getResources().openRawResource(location);
                    final byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                    inputImage.encodeContent(photoData);

                    final GoogleRunnable google = new GoogleRunnable(inputImage);

                    AsyncTask.execute(google);

                    while (google.textDetect == null || google.webDetect == null) { }

                    Log.i(TAG, "Returned Shit: \nText:\n" + google.textDetect + "\nWeb Type:\n" + google.webDetect);
                    String t1 = ""+google.textDetect;
                    String t2 = ""+google.webDetect;
                 //   System.out.println("Google Output" + google.textDetect + " " + google.webDetect + " " + location);
                 ///   final SpeakingAndroid speak = new SpeakingAndroid( t1,  t2, location);
                   // System.out.println("Google Output2" + google.textDetect + " " + google.webDetect + " " + location);
                    final Intent toSpeaker = new Intent(MainActivity.this, SpeakingAndroid.class);
                    toSpeaker.putExtra("WORD",t1);
                    toSpeaker.putExtra("WEB",t2);
                    toSpeaker.putExtra("IMAGE",location);
                    startActivity(toSpeaker);

                } catch (IOException ex) {
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
