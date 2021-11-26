package com.example.springdevice.main;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientStart {

    public static void main(String[] args) throws InterruptedException, URISyntaxException, JSONException {

        final MyClient clientEndPoint = new MyClient(new URI("ws://194.47.45.129:8080/websocket"));
        clientEndPoint.addMessageHandler(new MyClient.MessageHandler() {
            public void handleMessage(String message) {

            }
        });

        while (true){

            clientEndPoint.sendMessage("establishConnection");
            Thread.sleep(30000);

        }


    }

}



