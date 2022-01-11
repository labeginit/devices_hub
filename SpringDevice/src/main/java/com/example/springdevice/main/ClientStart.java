package com.example.springdevice.main;

import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientStart {


    public static void main(String[] args) throws InterruptedException, URISyntaxException, JSONException {
        MyClient.getInstance().start(new URI("ws://194.47.32.163:1337/websocket"));
        final MyClient clientEndPoint = MyClient.getInstance();
        //ro01.beginit.se:1337
        //194.47.44.10:1337
//ws://ro01.beginit.se:1337/websocket

        clientEndPoint.addMessageHandler(new MyClient.MessageHandler() {
            public void handleMessage(String message) {

            }
        });

        System.out.println("connected");

        while (true){
            clientEndPoint.sendMessage("");
            Thread.sleep(30000);

        }


    }



}



