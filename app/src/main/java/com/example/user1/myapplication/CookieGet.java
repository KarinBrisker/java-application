package com.example.user1.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CookieGet {

    public static String cookie = null;

    public CookieGet(){
        ServerCook ser = new ServerCook();
        ser.execute();
    }


    public class ServerCook extends AsyncTask<Void, Void, String> {

        ServerCook(){
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:8080/ServerProj/ServerLogin");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                try {
                    urlConnection.getInputStream();
                    CookieGet.cookie = urlConnection.getHeaderField("Set-Cookie");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}






