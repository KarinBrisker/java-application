package com.example.user1.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.GregorianCalendar;


public class MainActivity extends BaseActivity implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 6000;

    SensorManager sensorManager;
    float last_x,last_y,last_z;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastUpdate = System.currentTimeMillis();

        Fragment newFragment = new MainFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            ft.add(R.id.menuFragment, new MenuFragment());
//        }

        ft.add(R.id.mainFragment, newFragment);

        ft.commit();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ActionBar ab=getSupportActionBar();
        TextView textview=new TextView(getApplicationContext());
        LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setGravity(Gravity.CENTER);
        textview.setText(ab.getTitle().toString());
        textview.setTextSize(20);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(textview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        long curTime = System.currentTimeMillis();
        // only allow one update every 100ms.
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = values[0];
            float y = values[1];
            float z = values[2];

            float speed = Math.abs(x+y+z-last_x-last_y-last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                Long timeToAlert = new GregorianCalendar().getTimeInMillis() + 5000;

                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("sender", "one");


                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeToAlert, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
/*
                Intent intent2 = new Intent(this, NotificationReceiver.class);
                intent2.putExtra("sender", "two");
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeToAlert+5000, 30000, PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
                //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToAlert+5000, 30000, PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
*/
                Toast.makeText(this, "Alarms were set", Toast.LENGTH_SHORT).show();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
