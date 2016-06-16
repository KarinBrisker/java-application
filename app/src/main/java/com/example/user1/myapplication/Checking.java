package com.example.user1.myapplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Checking extends Activity {

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);


        final Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myFunction();
            }
        });
    }

    private void myFunction(){


        String hostName = "127.0.0.1";
        int portNumber = 5555;
        Queue<String> messagesRecieved = new LinkedList<String>();
        String msg = null;
        Socket echoSocket;
        PrintWriter writeToSocket = null;
        BufferedReader readFromSocket = null;
        try {
            echoSocket = new Socket(hostName, portNumber);
            writeToSocket = new PrintWriter(echoSocket.getOutputStream(), true);
            readFromSocket = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException ex) {
            System.out.print("beacusexxxxxxxxxxxxxxxxxxxxxxxxxx\n");
            System.out.print(ex.getCause());
        }

        //start sending
        writeToSocket.println("healoooo\n");

        checkStr();

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