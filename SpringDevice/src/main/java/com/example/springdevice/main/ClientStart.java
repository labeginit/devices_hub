package com.example.springdevice.main;
import arduino.Arduino;
import com.example.springdevice.service.ArduinoService;
import com.example.springdevice.service.MyComPortListener;
import com.example.springdevice.service.SerialController;
import com.fazecast.jSerialComm.SerialPort;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientStart {
    public static SerialPort firstAvailableComPort;

    public static void main(String[] args) throws InterruptedException, URISyntaxException, JSONException {

        final MyClient clientEndPoint = new MyClient(new URI("ws://ro01.beginit.se:1337/websocket"));
        //ro01.beginit.se:1337
        SerialController serialController = new SerialController();
        serialController.setSerialPort(SerialPort.getCommPorts()[0]);

        SerialPort serialPort = serialController.getSerialPort();
        serialPort.openPort();

        System.out.println("com port open: " + serialPort.getDescriptivePortName());
        serialPort.addDataListener(serialController);




//        SerialPort[] allAvailableComPorts = SerialPort.getCommPorts();
//
//        for(SerialPort eachComPort:allAvailableComPorts)
//            System.out.println("List of all available serial ports: " + eachComPort.getDescriptivePortName());
//
//        firstAvailableComPort = allAvailableComPorts[0];
//
//        firstAvailableComPort.openPort();
//
//        System.out.println("Opened the first available serial port: " + firstAvailableComPort.getDescriptivePortName());
//
//        MyComPortListener listenerObject = new MyComPortListener();
//
//        firstAvailableComPort.addDataListener(listenerObject);



        clientEndPoint.addMessageHandler(new MyClient.MessageHandler() {
            public void handleMessage(String message) {

            }
        });

        System.out.println("connected");

        while (true){
            clientEndPoint.sendMessage("getDevices");
            clientEndPoint.sendMessage("getDevices");
            Thread.sleep(300000);

        }


    }



}



