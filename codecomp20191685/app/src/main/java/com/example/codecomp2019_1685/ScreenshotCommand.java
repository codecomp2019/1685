package com.example.codecomp2019_1685;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenshotCommand extends AppCompatActivity {
    // Constants
    private static final String TAG = ScreenshotCommand.class.getName();

    // Instances

    // Constructors
    public ScreenshotCommand() {
        Log.i(TAG, "Initializing.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenshot);

        final View content = findViewById(R.id.screenview).getRootView();
        content.setDrawingCacheEnabled(true);

        getScreen();
    }

    private void getScreen()
    {
        View content = findViewById(R.id.screenview);
        Bitmap bitmap = content.getDrawingCache();
        String filename = "test.png";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
