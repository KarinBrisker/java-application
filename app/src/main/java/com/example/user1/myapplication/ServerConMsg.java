package com.example.user1.myapplication;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConMsg extends AsyncTask<Void, Void, String> {

    String name;
    String msg;

    ServerConMsg(String name, String msg){
        this.name = name;
        this.msg = msg;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String nameStr = "UserName="+name;
            String msgStr = "&Msg=" + msg;

            String urlStr = "http://10.0.2.2:8080/ServerProj/ServerMsg?" +
                    nameStr+msgStr;
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String getStr = streamReader.readLine();
                return getStr;
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


    @Override
    protected void onPostExecute(String ansStr) {

        if(ansStr.equals("sign ok")){
            //todo - move to menu window
        }else{
            //todo
        }
    }
}
