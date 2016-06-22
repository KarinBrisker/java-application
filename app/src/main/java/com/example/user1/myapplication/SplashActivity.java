package com.example.user1.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * the splash activity
 * shoe four meeesage than if we have user
 * and pass check them with the server
 * if not go to login window
 */
public class SplashActivity extends NoActionBarActivity {
    ProgressBar bar;
    String name;
    String pass;
    Thread t;
    int finish = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bar = (ProgressBar) findViewById(R.id.bar);
        startMessage();
    }

    /**
     * start the new thread to run the messages
     */
    private void startMessage() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {

                String msg = "";
                for(int i =0; i<4; i++) {
                    //message
                    switch (i){
                        case 0:msg = getString(R.string.firstmsg);
                        break;
                        case 1:msg = getString(R.string.secondstring);
                            break;
                        case 2:msg = getString(R.string.thridstring);
                            break;
                        case 3:msg = getString(R.string.fourthstring);
                            break;
                    }
                    final String msg2 = msg;

                    //print to gui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView t = (TextView) findViewById(R.id.msgtext);
                            t.setText(msg2+"");
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

                Thread d = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                        //get from the phone the user and pass if have
                        name = settings.getString("NAME", null);
                        pass=settings.getString("PASSWORD", null);

                        if(name!=null && pass!=null ){
                            //check if they are correct - send them to server
                            //if yes we move to main window
                            ServerLogin ser = new ServerLogin();
                            ser.execute();
                        }else{
                            Intent i = new Intent(SplashActivity.this, ExplainActivity.class);
                            startActivity(i);
                        }

                    }
                });

                d.start();

            }
        });

        t.start();
    }


    /***
     * the server
     */
    public class ServerLogin extends AsyncTask<Void, Void, String> {


        ServerLogin(){
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String userStr = "UserName="+name;
                String passStr = "&Password=" + pass;

                String urlStr = "http://10.0.2.2:8080/ServerProj/ServerLogin?" +
                        userStr+passStr;
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
                    Intent i = new Intent(SplashActivity.this, ExplainActivity.class);
                    startActivity(i);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Intent i = new Intent(SplashActivity.this, ExplainActivity.class);
                startActivity(i);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String ansStr) {
            if(ansStr.equals("login ok")){

                //get coockies
                CookieGet ck = new CookieGet();

                //move to main window
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
            else if(ansStr.equals("login error")){
                Intent i = new Intent(SplashActivity.this, ExplainActivity.class);
                startActivity(i);
            }
        }
    }




}