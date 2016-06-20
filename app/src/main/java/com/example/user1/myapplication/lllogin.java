

package com.example.user1.myapplication;
import android.app.ActionBar;
    import java.io.BufferedInputStream;
    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.ArrayList;

//    import org.apache.http.NameValuePair;
//    import org.apache.http.message.BasicNameValuePair;
//
//    import com.example.firstapp.R;

    import android.annotation.SuppressLint;
   // import android.app.ActionBar;
    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
    import android.view.Window;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;



    import android.view.Gravity;
    import android.widget.RelativeLayout;
    import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.user1.myapplication.R;


//   @SuppressLint("NewApi")
    public class lllogin extends AppCompatActivity {
        EditText un, pw;
        TextView error;
        Button login,signup;
        private String name,password;
        private String resp;
        private String errorMsg;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_ACTION_BAR);
            setContentView(R.layout.activity_lllogin);
            android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        //    ActionBar actionbar=this.getActionBar();
            TextView textview = new TextView(getApplicationContext());
            ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            p.gravity = Gravity.CENTER;
            textview.setLayoutParams(p);
            textview.setGravity(Gravity.CENTER);
            textview.setText(actionbar.getTitle().toString());
            textview.setTextSize(20);
            actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionbar.setCustomView(textview);

            un = (EditText) findViewById(R.id.et_un);
            pw = (EditText) findViewById(R.id.et_pw);
            login = (Button) findViewById(R.id.btn_login);
            signup=(Button)findViewById(R.id.btn_signupp);
            error = (TextView) findViewById(R.id.tv_error);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name=un.getText().toString();
                    password=pw.getText().toString();

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("NAME",name);
//                    //send to server
                    ServerLogin ser = new ServerLogin();
                    ser.execute();

                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(lllogin.this, SignupActivity.class);
                    startActivity(i);
                }
            });

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
                 String passStr = "&Password=" + password;

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
             if(ansStr.equals("login ok")){
                 //get coockies
                 CookieGet ck = new CookieGet();

                 // if ok we keep the parameters in the phone
                 SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                 SharedPreferences.Editor editor = settings.edit();
                 editor.putString("NAME",name);
                 editor.putString("PASSWORD",password);
// Apply the edits!
                 editor.apply();
                     Intent i = new Intent(lllogin.this, MainActivity.class);
                     startActivity(i);
                 }
                 else if(ansStr.equals("login error")){
                 Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_LONG).show();
                 }
             //todo - "login ok" - can continue
             //       "login error" - cant continue - try again
         }
     }

    }