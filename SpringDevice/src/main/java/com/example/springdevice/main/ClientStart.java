package com.example.springdevice.main;
import arduino.Arduino;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientStart {


    public static void main(String[] args) throws InterruptedException, URISyntaxException, JSONException {

        final MyClient clientEndPoint = new MyClient(new URI("ws://ro01.beginit.se:1337/websocket"));
        //ro01.beginit.se:1337


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



