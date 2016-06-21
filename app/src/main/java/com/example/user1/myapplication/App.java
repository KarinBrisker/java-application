package com.example.user1.myapplication;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;

public class App extends Application {
    @Override
    public void onCreate() {
        //check git
        super.onCreate();

        CookieHandler.setDefault(new CookieManager());
    }
}
