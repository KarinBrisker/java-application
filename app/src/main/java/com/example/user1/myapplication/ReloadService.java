package com.example.user1.myapplication;

import android.app.IntentService;
import android.content.Intent;


public class ReloadService extends IntentService
{
    public static final String DONE = "com.biu.ap2.fakebook.Services.ReloadService.DONE";

    public ReloadService() {
        super(ReloadService.class.getName());
    }

    public ReloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(DONE);
        this.sendBroadcast(i);
    }
}
