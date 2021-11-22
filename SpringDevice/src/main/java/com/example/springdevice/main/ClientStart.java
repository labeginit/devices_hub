package com.example.springdevice.main;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientStart {
    public static void main(String[] args) throws InterruptedException, URISyntaxException {


    final MyClient clientEndPoint = new MyClient(new URI("ws://localhost:1337/websocket"));
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
