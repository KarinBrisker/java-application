package com.example.user1.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ServerCheck extends Activity {

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servercheck);

        checkStr();

        final Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("****************************Clicked\n");
                ServerConLogin a = new ServerConLogin();
                a.execute();
            }
        });
    }

    private void checkStr() {
        final Button btn = (Button) findViewById(R.id.btnSkip);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView str = (TextView) findViewById(R.id.textCheck);
                str.setText("message sent");
            }
        });
    }

}