package com.example.user1.myapplication;

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
    import android.app.ActionBar;
    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.ActionBarActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;



    import android.view.Gravity;
    import android.widget.RelativeLayout;
    import android.widget.RelativeLayout.LayoutParams;


 //   @SuppressLint("NewApi")
    public class lllogin extends ActionBarActivity {
        EditText un, pw;
        TextView error;
        Button ok;
        private String name,password;
        private String resp;
        private String errorMsg;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lllogin);
            un = (EditText) findViewById(R.id.et_un);
            pw = (EditText) findViewById(R.id.et_pw);
            ok = (Button) findViewById(R.id.btn_login);
            error = (TextView) findViewById(R.id.tv_error);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name=un.getText().toString();
                    password=pw.getText().toString();

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("NAME",name);

// Apply the edits!
                    editor.apply();
                    Intent i = new Intent(lllogin.this, MainActivity.class);
                    startActivity(i);

                }
            });
//            ActionBar ab=getActionBar();
//            TextView textview=new TextView(getApplicationContext());
//            LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//            textview.setLayoutParams(layoutparams);
//            textview.setGravity(Gravity.CENTER);
//            textview.setText(ab.getTitle().toString());
//            textview.setTextSize(20);
//            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            ab.setCustomView(textview);
          //  setTitle("@string/app_name");

        }


//            ok.setOnClickListener(new View.OnClickListener() {

//                @Override
//                public void onClick(View v) {
//                    /** According with the new StrictGuard policy,  running long tasks on the Main UI thread is not possible
//                     So creating new thread to create and execute http operations */
//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//                            postParameters.add(new BasicNameValuePair("username",un.getText().toString()));
//                            postParameters.add(new BasicNameValuePair("password",pw.getText().toString()));
//
//                            String response = null;
//                            try {
//                                response = SimpleHttpClient.executeHttpPost("http://192.168.1.3:8084/LoginServer/login.do", postParameters);
//                                String res = response.toString();
//                                resp = res.replaceAll("\\s+", "");
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                errorMsg = e.getMessage();
//                            }
//                        }
//
//                    }).start();
//                    try {
//                        /** wait a second to get response from server */
//                        Thread.sleep(1000);
//                        /** Inside the new thread we cannot update the main thread
//                         So updating the main thread outside the new thread */
//
//                        error.setText(resp);
//
//                        if (null != errorMsg && !errorMsg.isEmpty()) {
//                            error.setText(errorMsg);
//                        }
//                    } catch (Exception e) {
//                        error.setText(e.getMessage());
//                    }
//                }
//            });


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

             //todo - "login ok" - can continue
             //       "login error" - cant continue - try again
         }
     }

    }