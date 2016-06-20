package com.example.user1.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;


import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SignupActivity extends AppCompatActivity {


    EditText un, em, nn,pw;
    private String user_name,password,nick_name,icon,email;
    TextView error,clean;
    Button ok;
    private String resp;
    private String errorMsg;
    RadioGroup rg;
    String radiovalue;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_signup);
        un = (EditText) findViewById(R.id.fullName);
        un.requestFocus();
        em = (EditText) findViewById(R.id.input_email);
        nn = (EditText) findViewById(R.id.input_name);
        pw = (EditText) findViewById(R.id.input_password);
        clean =(TextView) findViewById(R.id.link_clean);
        rg =(RadioGroup)findViewById(R.id.rg_id);
        ok = (Button) findViewById(R.id.btn_signup);
        error = (TextView) findViewById(R.id.tv_error);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un.setText("");
                pw.setText("");
                em.setText("");
                nn.setText("");
            }
        });



        final RadioButton boyButton = (RadioButton)findViewById(R.id.boy);
        boyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

            }
        });
        final Button girlButton = (RadioButton)findViewById(R.id.girl);
        girlButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            }
        });
  //      SetActionBar();
//
        android.support.v7.app.ActionBar ab=getSupportActionBar();

        TextView textview=new TextView(getApplicationContext());
        LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setGravity(Gravity.CENTER);
        textview.setText(ab.getTitle().toString());
        textview.setTextSize(20);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(textview);


    }
//    private void SetActionBar(){
//        ActionBar ab=getActionBar();
//        ab.setTitle("@string/app_name");
//    }


    public boolean validate() {
        boolean valid = true;

        String name = un.getText().toString();
        String email = em.getText().toString();
        String password = pw.getText().toString();
        String nickname=nn.getText().toString();
        if (name.isEmpty() || name.length() < 1) {
            un.setError("at least one character");
            valid = false;
        } else {
            un.setError(null);
            user_name=un.getText().toString();
        }

        if (nickname.isEmpty() || nickname.length() < 1) {
            nn.setError("at least one character");
            valid = false;
        } else {
            nn.setError(null);
            nickname=nn.getText().toString();
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            em.setError("enter a valid email address");
            valid = false;
        } else {
            em.setError(null);
            email=em.getText().toString();
        }

        if (password.isEmpty() || password.length() < 1 || password.length() > 10) {
            pw.setError("between 1 and 10 alphanumeric characters");
            valid = false;
        } else {
            pw.setError(null);
            password=pw.getText().toString();
        }

        if(rg.getCheckedRadioButtonId()==-1){
            valid = false;
        }
        else {
           icon =((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        }



        return valid;
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        ok.setEnabled(false);
        // TODO: Need to send to server. if good: session
        //
//        ServerSign ss=new ServerSign();
//        ss.execute();
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        ok.setEnabled(true);
    }


    /***
     * the server
     */
    public class ServerSign extends AsyncTask<Void, Void, String> {


        ServerSign(){
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String userStr = "UserName="+user_name;
                String passStr = "&Password=" + password;
                String realNameStr = "&RealName=" + nick_name;
                String mailStr = "&Mail=" + email;
                String iconStr = "&Img=" + icon;

                //localhost:8080/ServerProj/ServerSign?UserName=galit&Password=1122&RealName=alma&Mail=galitgmailcom&Img=supergirlpng

                String urlStr = "http://10.0.2.2:8080/ServerProj/ServerSign?" +
                        userStr+passStr+realNameStr+mailStr+iconStr;
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
            if(ansStr.equals("sign ok"))
            {
                //get coockies
                CookieGet ck = new CookieGet();

                // if succeeded sign up keep details in phone
                SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("NAME",user_name);
                editor.putString("PASSWORD",password);
// Apply the edits!
                editor.apply();
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
            else if(ansStr.equals("sign error")){
                onSignupFailed();
            }
            //todo - "sign ok" - can continue
            //       "sign error" - cant continue - try again
        }
    }
}