package com.example.user1.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class LocalService extends Service {
    public static String BROADCAST_ACTION = "ap2.biu.poker.new_move";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String hostName = "127.0.0.1";
                int portNumber = 5555;
                Queue<String> messagesRecieved = new LinkedList<String>();
                int flag = 0;
                String msg = null;
                Socket echoSocket;
                PrintWriter writeToSocket;
                BufferedReader readFromSocket = null;
                try {
                    echoSocket = new Socket(hostName, portNumber);
                    writeToSocket = new PrintWriter(echoSocket.getOutputStream(), true);
                    readFromSocket = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                } catch (IOException ex) {
                    System.out.print("beacuse\n");
                    System.out.print(ex.getCause());
                }

                //start reading
                while (true) {
                    char[] stringData = new char[4096];
                    try {
                        readFromSocket.read(stringData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String dataStr = new String(stringData);
                    if (dataStr != null) {
                        if (dataStr.equals("\"empty\"") == false) {
                            //messagesRecieved.add(dataStr);
                            flag = 1;
                            msg = dataStr;
                        }
                    }
                }
            }
        });

        thread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}