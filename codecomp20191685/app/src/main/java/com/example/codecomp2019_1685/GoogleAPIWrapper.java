package com.example.codecomp2019_1685;

import android.util.Log;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

public class GoogleAPIWrapper extends Object {
    // Constants
    private static String API_KEY = "AIzaSyDU3kVloR3wQS_VDOXabEftoG5zH3uJQ80";
    private static final String TAG = GoogleAPIWrapper.class.getName();

    // Instances
    private Vision.Builder visionBuilder;
    private Vision vision;

    // Constructors
    public GoogleAPIWrapper() {
        Log.i(TAG, "Initializing.");
        this.vision = initVision();
        return;
    }

    // Methods
    private Vision initVision() {
        Log.i(TAG, "Building vision.");
        this.visionBuilder  = new Vision.Builder(
            new NetHttpTransport(),
            new AndroidJsonFactory(),
        null);
        this.visionBuilder.setVisionRequestInitializer(
            new VisionRequestInitializer(API_KEY));
        Log.i(TAG, "Built vision.");
        return this.visionBuilder.build();
    }
}