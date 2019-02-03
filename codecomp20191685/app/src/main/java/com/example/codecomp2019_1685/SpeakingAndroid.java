package com.example.codecomp2019_1685;
import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpeakingAndroid extends Activity implements  OnInitListener {

    private static final String TAG = SpeakingAndroid.class.getName();

    // Instances
    private TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 421;
    private String words;
    private String web;
    // private byte[] theMemeImage;

    //create the Activity
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.output);
        words = getIntent().getStringExtra("WORD");
        web = getIntent().getStringExtra("WEB");
        // theMemeImage = getIntent().getByteArrayExtra("IMAGE");
        TextView meme = (TextView) findViewById(R.id.transcribe);
        meme.setText(words);
        TextView webs = (TextView) findViewById(R.id.web);
        webs.setText(web);
        ImageView imag = (ImageView) findViewById(R.id.meme);
        // final Bitmap bmp = BitmapFactory.decodeByteArray(theMemeImage, 0, theMemeImage.length);
        // imag.setImageBitmap(bmp);
        //get a reference to the button element listed in the XML layout
        System.out.println(words +" " + web +"HELLO");
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // replace this Locale with whatever you want                    
                    Locale localeToUse = new Locale("en","US");
                    myTTS.setLanguage(localeToUse);
                    myTTS.speak(words +" " + web, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

    }

    //speak the user text
    private void speakWords(String speech) {

        Log.i(TAG, " " + speech);
        //speak straight away
        int success = 0;

        //myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        success = myTTS.speak(speech,0, null,null);
        //myTTS.shutdown();
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //setup TTS
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
            {
                myTTS.setLanguage(Locale.US);
            }
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}


