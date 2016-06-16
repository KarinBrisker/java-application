package com.example.user1.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Random;

public class MySplash extends Activity {
    int counter;
    Random random = new Random();
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_splash);

        bar = (ProgressBar) findViewById(R.id.bar);
        startProgressBar();
    }
    private void startProgressBar() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter < 100) {
                    counter += random.nextInt(25);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(counter);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

                Intent i = new Intent(MySplash.this, MainActivity.class);
                startActivity(i);
            }
        });
        t.start();
    }
}