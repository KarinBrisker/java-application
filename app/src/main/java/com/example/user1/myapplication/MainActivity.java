package com.example.user1.myapplication;
import android.app.Application;
import android.app.Fragment;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import 	android.widget.ImageView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends BaseActivity  {

    private SensorManager sensorMgr;
    private ShakeListener listener;
    private Sensor accelerometer;
    private Button postButton;
    float last_x,last_y,last_z;
    private long lastUpdate;
    private EditText post_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isEng = Locale.getDefault().getLanguage().equals("en");
        String lan=Locale.getDefault().getLanguage().toString();
        if(isEng==true){
            setContentView(R.layout.activity_main_eng);
        }
        else {
            setContentView(R.layout.activity_main);
        }
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
        listener = new ShakeListener();
            lastUpdate = System.currentTimeMillis();
            post_txt = (EditText) findViewById(R.id.text_post);
            final Fragment newFragment = new MainFragment();
            final FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mainFragment, newFragment);
            ft.commit();
            ActionBar ab=getSupportActionBar();
            TextView textview=new TextView(getApplicationContext());
            LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            textview.setLayoutParams(layoutparams);
            textview.setGravity(Gravity.CENTER);
            textview.setText(ab.getTitle().toString());
            textview.setTextSize(20);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(textview);
            postButton = (Button) findViewById(R.id.postButton);
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String new_post=post_txt.getText().toString();
                    MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.mainFragment);
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                    String name = settings.getString("NAME", null);
                    fragment.generateFakePosts(1,name,currentDateTimeString,new_post);
                    post_txt.setText("");




                    // TODO: To update the server
                }
            });

    }

    private void changeActivity(int btnResource, final Class<?> activityClass) {
        Button btn = (Button) findViewById(btnResource);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, activityClass);
                startActivity(i);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        //setContentView(R.layout.main);
        setTitle(R.string.app_name);

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            setContentView(R.layout.activity_main_eng);
        } else{
            setContentView(R.layout.activity_main);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(listener);
    }

    class ShakeListener implements SensorEventListener{
        private static final int FORCE_THRESHOLD = 1500, TIME_THRESHOLD = 100, SHAKE_TIMEOUT = 500;
        private static final int SHAKE_DURATION = 1000, SHAKE_COUNT = 3;
        private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
        private int mShakeCount = 0;
        private long mLastShake,mLastForce,mLastTime;

        @Override
        public void onSensorChanged(SensorEvent event) {
            long now = System.currentTimeMillis();
            if ((now - mLastForce) > SHAKE_TIMEOUT) {mShakeCount = 0;}

            float[] values = event.values;
            if ((now - mLastTime) > TIME_THRESHOLD) {
                long diff = now - mLastTime;
                float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {
                    if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                        mLastShake = now;
                        mShakeCount = 0;
                        Toast.makeText(MainActivity.this, "Loading...!", Toast.LENGTH_LONG).show();
                        // TODO:to check if there is new data from server

                        // if there is, add it

                        // you can do it like this:

//                             String new_post=...
//                        MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.mainFragment);
//                        String currentDateTimeString = ...
//                        String name = ...
//                        fragment.generateFakePosts(1,name,currentDateTimeString,new_post);




                    }
                    mLastForce = now;
                }
                mLastTime = now;
                mLastX = values[SensorManager.DATA_X];
                mLastY = values[SensorManager.DATA_Y];
                mLastZ = values[SensorManager.DATA_Z];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }
}
/*
public class MainActivity extends AppCompatActivity implements View.OnTouchListener  {

    Button postButton;
    float last_x,last_y,last_z;
    private long lastUpdate;
    EditText post_txt;

    private SensorManager sensorMgr;
    private ShakeListener listener;
    private Sensor accelerometer;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        //setContentView(R.layout.main);
        setTitle(R.string.app_name);

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            setContentView(R.layout.activity_main_eng);
        } else{
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isEng = Locale.getDefault().getLanguage().equals("en");
        String lan=Locale.getDefault().getLanguage().toString();
        if(isEng==true){
            setContentView(R.layout.activity_main_eng);
        }
        else {
            setContentView(R.layout.activity_main);
        }




        lastUpdate = System.currentTimeMillis();
        post_txt = (EditText) findViewById(R.id.text_post);
        final Fragment newFragment = new MainFragment();
        final FragmentManager fm = getFragmentManager();
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

        postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_post=post_txt.getText().toString();
                MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.mainFragment);
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                String name = settings.getString("NAME", null);
                fragment.generateFakePosts(1,name,currentDateTimeString,new_post);
                post_txt.setText("");
            }
        });
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShake(){
                Toast.makeText(getBaseContext(), "Shake!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(mSensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
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
        sensorManager.unregisterListener(mSensorListener);
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
//                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//
//                Long timeToAlert = new GregorianCalendar().getTimeInMillis() + 5000;
//
//                Intent intent = new Intent(this, NotificationReceiver.class);
//                intent.putExtra("sender", "one");
//
//
//                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeToAlert, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
/*
                Intent intent2 = new Intent(this, NotificationReceiver.class);
                intent2.putExtra("sender", "two");
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeToAlert+5000, 30000, PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
                //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToAlert+5000, 30000, PendingIntent.getBroadcast(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
*/
 /*
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
 */