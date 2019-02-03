package com.example.codecomp2019_1685;
import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;

import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codecomp2019_1685.R;
import com.google.api.services.vision.v1.model.Image;

public class SpeakingAndroid extends Activity implements  OnInitListener {

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    String words ;
    String web;
    int theMemeImage ;

   /* public SpeakingAndroid() { }

    public SpeakingAndroid(String text, String web, int image)
    {
        this();
        this.words = text;
        this.web = web;
        this.theMemeImage = image;
        System.out.println("HELLO" + text + " " + web + "DATAD");
    }*/


    //create the Activity
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.output);
        words = getIntent().getStringExtra("WORD");
        web = getIntent().getStringExtra("WEB");
        theMemeImage = getIntent().getIntExtra("IMAGE", 0);
        TextView meme = (TextView) findViewById(R.id.transcribe);
        meme.setText(words);
        TextView webs = (TextView) findViewById(R.id.web);
        webs.setText(web);
        ImageView imag = (ImageView) findViewById(R.id.meme);
        imag.setImageResource(this.theMemeImage);
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
       // speakWords("hello there");
/*
        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);*/
    }
/*
    //respond to button clicks
    public void onClick(View v) {

        //get the text entered
        EditText enteredText = (EditText)findViewById(R.id.enter);
        String words = enteredText.getText().toString();
        speakWords(words);
    }
*/

    //speak the user text
    private void speakWords(String speech) {

        Log.d("speakWords", " "+speech);
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
            if(myTTS.isLanguageAvailable(Locale.CANADA)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.CANADA);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}


