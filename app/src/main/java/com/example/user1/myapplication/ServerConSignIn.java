package com.example.user1.myapplication;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConSignIn extends AsyncTask<Void, Void, String> {

    String name;
    String pass;
    String realName;
    String mail;
    String img;

    ServerConSignIn(String name, String pass, String realName, String mail, String img){
        this.name = name;
        this.pass = pass;
        this.realName = realName;
        this.mail = mail;
        this.img = img;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String nameStr = "UserName="+name;
            String passStr = "&Password=" + pass;
            String realStr = "&RealName="+realName;
            String mailStr = "&Mail="+mail;
            String imgStr = "&Img="+img;

            String urlStr = "http://10.0.2.2:8080/ServerProj/ServerSign?" +
                    nameStr+passStr+realStr+mailStr+imgStr;
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
