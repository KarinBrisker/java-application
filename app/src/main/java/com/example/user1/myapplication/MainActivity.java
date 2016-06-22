package com.example.user1.myapplication;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends BaseActivity  {

    private SensorManager sensorMgr;
    private ShakeListener listener;
    private Sensor accelerometer;
    private Button postButton;
    float last_x,last_y,last_z;
    private long lastUpdate;
    private EditText post_txt;
    private  AlarmManager alarmMgr=null;
   private PendingIntent pi = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_eng);
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

                    //send the msg to server
                    ServerMsg ser = new ServerMsg(name, new_post, currentDateTimeString);
                    ser.execute();

                }
            });
         alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
      //  Long timeToAlert = new GregorianCalendar().getTimeInMillis() + 5000*60;
        Intent intent = new Intent(this, NotificationReceiver.class);
              //  intent.putExtra("sender", "one");
        pi=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, new GregorianCalendar().getTimeInMillis(),5000*60, pi);
      //  Toast.makeText(this, "Alarms were set", Toast.LENGTH_SHORT).show();
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
        if(alarmMgr!=null&&pi!=null){
            alarmMgr.cancel(pi);
        }
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

                        Toast.makeText(MainActivity.this, getResources().getString(R.string.loading), Toast.LENGTH_LONG).show();
                        //ask the server for the messages
                        ServerGetMsg serGet = new ServerGetMsg();
                        serGet.execute();

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



    /***
     * the server
     */
    public class ServerMsg extends AsyncTask<Void, Void, String> {

        //localhost:8080/ServerProj/ServerMsg?UserName=galit&Time=15:24&Msg=helloYou

        String name;
        String post;
        String time;

        ServerMsg(String name, String post, String time){
            this.name = name.replace(" ", "%20");
            this.post = post.replace(" ", "%20");
            this.time = time.replace(" ", "%20");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String userStr = "UserName="+name;
                String timeStr = "&Time=" + time;
                String postStr = "&Msg=" + post;

                String urlStr = "http://10.0.2.2:8080/ServerProj/ServerMsg?" +
                        userStr+timeStr+postStr;
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestProperty("Cookie", CookieGet.cookie);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    String getStr = streamReader.readLine();
                    return getStr;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Srerver connnection problem", Toast.LENGTH_LONG).show();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Srerver connnection problem", Toast.LENGTH_LONG).show();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String ansStr) {
            if(ansStr.equals("msg ok")){
            }
            else if(ansStr.equals("msg error")){
            }
        }
    }


    /**
     * ask the server for updates
     * the server
     */
    public class ServerGetMsg extends AsyncTask<Void, Void, String> {

        List<MsgClass> msgList;

        //localhost:8080/ServerProj/ServerMsg?getMsg

        ServerGetMsg(){
        }

        @Override
        protected String doInBackground(Void... params) {

            String getStr = null;
            List<MsgClass> msgList;

            try {

                String urlStr = "http://10.0.2.2:8080/ServerProj/ServerMsg?getMsg";
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestProperty("Cookie", CookieGet.cookie);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    getStr = streamReader.readLine();
                    return getStr;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Srerver connnection problem", Toast.LENGTH_LONG).show();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Srerver connnection problem", Toast.LENGTH_LONG).show();
            }
            return getStr;
        }


        @Override
        protected void onPostExecute(String ansStr) {
            if(!ansStr.equals("msg error")){

                //convert to json
                try {
                    //get the msg list
                    JSONObject gson = new JSONObject(ansStr);
                    int size = gson.getInt("size");
                    msgList = new ArrayList<>();
                    MsgClass msg;
                    String user;
                    String time;
                    String post;
                    JSONObject json2;

                    for(int i=0; i<size; i++){
                        //get by index
                        String ind = String.valueOf(i);
                        json2 = (JSONObject) gson.get(ind);
                        //get params
                        user = (String) json2.get("user");
                        time = (String) json2.get("time");
                        post = (String) json2.get("msg");
                        //set to class msg
                        msg = new MsgClass();
                        msg.setUser(user);
                        msg.setTime(time);
                        msg.setMsg(post);
                        //add to list
                        msgList.add(msg);
                    }
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + ansStr + "\"");
                }

                final FragmentManager fm = getFragmentManager();
                MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.mainFragment);

                for (MsgClass msg : msgList){

                    if(fragment.checkIfExist(msg)){
                        continue;
                    }

                    String new_post=msg.getMsg();
                    String currentDateTimeString = msg.getTime();
                    String name = msg.getUser();
                    fragment.generateFakePosts(1,name,currentDateTimeString,new_post);
                }
            }
        }
    }


}