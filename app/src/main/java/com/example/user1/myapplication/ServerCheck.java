package com.example.user1.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerCheck extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servercheck);


        final Button btn = (Button) findViewById(R.id.btnSend);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyCheck();
            }


        });

    }

    private void keyCheck() {

        EditText str = (EditText) findViewById(R.id.editText);
        String msg = str.getText().toString();
        msg = msg.replace(" ", "$");

        TextView view = (TextView) findViewById(R.id.textResponse);
        view.setText("clicked");

        ServerConMsg ser = new ServerConMsg("liron", msg);
        ser.execute();
    }



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

            TextView view = (TextView) findViewById(R.id.textResponse);
            view.setText(ansStr);
        }
    }


}
