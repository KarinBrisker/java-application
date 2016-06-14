package com.example.user1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class SplashActivity extends NoActionBarActivity {
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bar = (ProgressBar) findViewById(R.id.bar);
        startMessage();
    }


    private void startMessage() {
        Thread t = new Thread(new Runnable() {
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

                //moving to other window
                Intent i = new Intent(SplashActivity.this, ExplainActivity.class);
                startActivity(i);
            }
        });



        t.start();
    }
}