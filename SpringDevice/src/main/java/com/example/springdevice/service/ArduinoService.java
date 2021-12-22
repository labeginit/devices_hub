package com.example.springdevice.service;

import arduino.Arduino;
import com.example.springdevice.SpringDeviceApplication;
import com.example.springdevice.main.MyClient;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.*;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.websocket.Session;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Locale;


@Service
public class ArduinoService implements ArduinoConnect {
    String readline = "";
    String portName = "COM3";
    private byte[] readBuffer = new byte[400];
    private InputStream input;
    private String temperatureBuffer;
    SerialPort serialPort;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;
    SpringDeviceApplication one = new SpringDeviceApplication();

    private static final String[] PORT_NAMES = {"COM3"};//ändra här om COM strular och säger tex COM3 istället för COM4
    private static final Arduino AdruinoCon = new Arduino("COM3", 9600);
    Session userSession = null;


    private boolean isOn; // state of the led
    String[] commands = {"off", "on"}; // commands that adruino can recognize
    String[] commandsInside = {"insideOff", "insideOn"};
    String[] commandsAlarm = {"alarmOn", "alarmOff"};
    String[] commandsTemp = {"temperature"};
    String[] commandsFan = {"fan"};
    String[] commandsFanHigh = {"High"};
    String[] commandsFanMedium = {"Medium"};
    String[] commandsFanLow = {"Low"};
    String[] commandsFanOff = {"Off"};
    String[] commandsHeater = {"heaterOn", "heaterOff"};

    public void smartHouse() {
       AdruinoCon.openConnection();
       // recivietemp();
    }
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    @Override
    public void ledOn() {
        // AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }

    @Override
    public void temp() {

    }


    @Override
    public void ledOff() {
        //AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void heater() throws InterruptedException {
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsHeater[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void heaterOff() throws InterruptedException {
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsHeater[commandIndex]); // pick a command from an array and send it to USB
    }


    @Override
    public void ledInsideOn() {
        //AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void ledInsideOff() throws InterruptedException {
         //AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }


    @Override
    public void alarmOn() throws InterruptedException {
        //AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void alarmOff() throws InterruptedException {
        //AdruinoCon.openConnection(); //open connection
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fan() throws InterruptedException {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanHigh[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanMedium() throws InterruptedException {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanMedium[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanOff() throws InterruptedException {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanOff[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanLow() throws InterruptedException {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanLow[commandIndex]); // pick a command from an array and send it to USB
    }


    protected void receive(String line) throws InterruptedException {
        System.out.println(line);
        System.out.println(line);
        MyClient.getInstance().rec(line);
//        myClient.sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + line + "'}");
//        System.out.println("i have sent IT");


//        if (readline.startsWith("temperature")) {
//            String[] words = readline.split(" ");
//            String str = words[1].replaceAll("[^\\.0123456789]", ""); // remove all non-digits
//            double temp = Double.parseDouble(str);
//            System.out.println("penis");
//            if (temp > 15) {
//                String formattedTemp = String.format(Locale.getDefault(), "%.1f", temp);
//                myClient.sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + formattedTemp + "'}");
//
//                System.out.println(formattedTemp);
//                System.out.println("i am in formated temp");
//
//            }
//
//        }
    }
}
