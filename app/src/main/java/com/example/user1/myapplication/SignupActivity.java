package com.example.user1.myapplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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


public class SignupActivity extends ActionBarActivity {


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
        setContentView(R.layout.activity_signup);
        un = (EditText) findViewById(R.id.fullName);
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
//        ActionBar ab=getActionBar();
//        TextView textview=new TextView(getApplicationContext());
//        LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        textview.setLayoutParams(layoutparams);
//        textview.setGravity(Gravity.CENTER);
//        textview.setText(ab.getTitle().toString());
//        textview.setTextSize(20);
//        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        ab.setCustomView(textview);


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
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        ok.setEnabled(true);
    }


}