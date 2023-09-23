package com.example.quickshopapp;

import android.app.Application;
import com.squareup.picasso.Picasso;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Picasso
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());

        // Any other global initialization code can go here
    }
}

