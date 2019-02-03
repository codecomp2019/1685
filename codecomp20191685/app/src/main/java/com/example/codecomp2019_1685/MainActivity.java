package com.example.codecomp2019_1685;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import com.google.api.services.vision.v1.model.Image;
import org.apache.commons.io.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = MainActivity.class.getName();
    private static final int MEME_SELECT_REQUEST_CODE = 360;
    private static final int MEME_CAMERA_REQUEST_CODE = 420;

    // Instances
    private TextToSpeech myTTS;
    private GoogleAPIWrapper googleAPI;
    private Vibrator haptic_feedback;

    // Constructors
    public MainActivity() {
        Log.i(TAG, "Initializing.");
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
                Log.e(TAG, "IO failure at " + ex.getMessage());
            }
            return;
        }
    }

    // Methods
    private void use_haptic_feedback() {
        haptic_feedback.vibrate(250);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // replace this Locale with whatever you want
                    Locale localeToUse = new Locale("en","US");
                    myTTS.setLanguage(localeToUse);
                    myTTS.speak("Welcome to Meme Machine", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        this.googleAPI = new GoogleAPIWrapper();
        this.haptic_feedback = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        this.use_haptic_feedback();
        this.use_haptic_feedback();
        this.use_haptic_feedback();

        /*if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(MainActivity.this)) {   //Android M Or Over
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 2038);
            return;
        }
        startService(new Intent(MainActivity.this, ButtonOverlay.class));*/

        final Button memeSelectButton = (Button) findViewById(R.id.memeselect);

        memeSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTTS.speak("Meme Select", TextToSpeech.QUEUE_FLUSH, null);
                MainActivity.this.use_haptic_feedback();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, MEME_SELECT_REQUEST_CODE);
            }
        });

        final Button memeCameraButton = (Button) findViewById(R.id.memecamera);

        memeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTTS.speak("Meme Camera", TextToSpeech.QUEUE_FLUSH, null);
                MainActivity.this.use_haptic_feedback();
                MainActivity.this.use_haptic_feedback();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, MEME_CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == MEME_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());

                final Image inputImage = new Image();

                try {
                    final InputStream inputStream = getContentResolver().openInputStream(uri);
                    final byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                    inputImage.encodeContent(photoData);

                    while (inputImage == null) { }
                    myTTS.speak("Processing meme.", TextToSpeech.QUEUE_FLUSH, null);
                    final GoogleRunnable google = new GoogleRunnable(inputImage);

                    this.use_haptic_feedback();

                    AsyncTask.execute(google);

                    this.use_haptic_feedback();

                    while (google.textDetect == null || google.webDetect == null) { }

                    Log.i(TAG, "Returned: \nText:\n" + google.textDetect + "\nWeb Type:\n" + google.webDetect);
                    String t1 = "" + google.textDetect;
                    String t2 = "" + google.webDetect;
                    final Intent toSpeaker = new Intent(MainActivity.this, SpeakingAndroid.class);
                    toSpeaker.putExtra("WORD", t1);
                    toSpeaker.putExtra("WEB", t2);
                    startActivity(toSpeaker);

                } catch (IOException ex) {
                    Log.e(TAG, "IO failure at " + ex.getMessage());
                }
            }
        }
        else if (requestCode == MEME_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Bitmap inputBitmap = (Bitmap) resultData.getExtras().get("data");
            final Image inputImage = new Image();

            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            inputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] photoData = stream.toByteArray();
            inputImage.encodeContent(photoData);

            while (inputImage == null) { }
            myTTS.speak("Processing meme.", TextToSpeech.QUEUE_FLUSH, null);
            final GoogleRunnable google = new GoogleRunnable(inputImage);

            this.use_haptic_feedback();

            AsyncTask.execute(google);

            this.use_haptic_feedback();

            while (google.textDetect == null || google.webDetect == null) { }

            Log.i(TAG, "Returned: \nText:\n" + google.textDetect + "\nWeb Type:\n" + google.webDetect);
            String t1 = "" + google.textDetect;
            String t2 = "" + google.webDetect;
            final Intent toSpeaker = new Intent(MainActivity.this, SpeakingAndroid.class);
            toSpeaker.putExtra("WORD", t1);
            toSpeaker.putExtra("WEB", t2);
            startActivity(toSpeaker);
        }
    }
}
