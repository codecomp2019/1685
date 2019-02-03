package com.example.codecomp2019_1685;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


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
        this.visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);
        this.visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer(API_KEY));
        Log.i(TAG, "Built vision.");
        return this.visionBuilder.build();
    }

    private AnnotateImageResponse requestAnnotation(Image img, String featureName) throws IOException {
        // Specify we want a text detection.
        final Feature desiredFeature = new Feature();
        desiredFeature.setType(featureName);

        // Assign the feature to a request.
        final AnnotateImageRequest request = new AnnotateImageRequest();
        request.setImage(img);
        request.setFeatures(Arrays.asList(desiredFeature));

        // Assign the requests into a batch.
        final BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
        batchRequest.setRequests(Arrays.asList(request));

        // Execute the batch.
        final BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();

        // Return the response.
        return batchResponse.getResponses().get(0);
    }

    public String computeTextDetection(Image img) throws IOException {
        final TextAnnotation annotation = requestAnnotation(img, "TEXT_DETECTION").getFullTextAnnotation();
        if (annotation != null) {
            return "" + annotation.getText();
        }
        return "";
    }

    public String computeWebDetection(Image img) throws IOException {
        final WebDetection annotation = requestAnnotation(img, "WEB_DETECTION").getWebDetection();
        final List<WebEntity> entities = annotation.getWebEntities();
        String text = "";
        if (entities != null) {
            final WebEntity entity = entities.get(0);
            text += "I guess at a " + entity.getScore() * 100 + "% probability, that this is " + entity.getDescription() + "\n";
        }
        return text;
    }
}