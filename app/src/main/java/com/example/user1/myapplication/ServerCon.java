package com.example.user1.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerCon implements Runnable {

    // writer to the server
    private PrintWriter writeToSocket;
    // reader from the server
    private BufferedReader readFromSocket;
    // messages queue
    final Queue<String> messagesRecieved;
    // message recieved
    public String msg;
    // instance
    private static ServerCon serverIns;
    // name game
    private String name;
    // maze name
    private String myMazeName;
    // socket
    private Socket echoSocket;


    public ServerCon() {

        String hostName = "127.0.0.1";
        int portNumber = 5555;
        messagesRecieved = new LinkedList<String>();
        msg = null;
        try {
            echoSocket = new Socket(hostName, portNumber);
            writeToSocket = new PrintWriter(echoSocket.getOutputStream(), true);
            readFromSocket = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ServerCon.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread a = new Thread(this);
        a.start();
    }

    // send to server
    public void sendToServer(String str) {
        writeToSocket.println(str);
    }

    // get message
    public String GetMessage() {
        if (messagesRecieved.size() > 0) {
            String jsonString = messagesRecieved.poll();
            return jsonString;
        }
        return null;
    }

    // get message
    public void readFromServer() throws IOException {
        while (true) {
            char[] stringData = new char[4096];
            readFromSocket.read(stringData);
            String dataStr = new String(stringData);
            if (dataStr != null) {
                if (dataStr.equals("\"empty\"") == false) {
                    messagesRecieved.add(dataStr);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            readFromServer();
        } catch (IOException ex) {
            Logger.getLogger(ServerCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
