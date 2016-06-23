package com.example.user1.myapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver
{
    Context context;
    Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.intent = intent;

        //get new msg from server and update user
        ServerGetMsg ser = new ServerGetMsg();
        ser.execute();

    }

    private void functionAfter() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.notiTitle))
                        .setContentText(context.getResources().getString(R.string.notiContent));

        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

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

                String urlStr = "http://advprog.cs.biu.ac.il:8080/ServerProj/ServerMsg?getMsg";
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
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
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

                for (MsgClass msg : msgList){


                    MainFragment fragment = (MainFragment)MainActivity.fm.findFragmentById(R.id.mainFragment);

                    if(fragment.checkIfExist(msg)){
                        continue;
                    }

                    String new_post=msg.getMsg();
                    String currentDateTimeString = msg.getTime();
                    String name = msg.getUser();
                    fragment.generateFakePosts(1,name,currentDateTimeString,new_post);

                }

                functionAfter();
            }
        }
    }


}
