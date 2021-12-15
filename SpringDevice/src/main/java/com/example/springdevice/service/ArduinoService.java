package com.example.springdevice.service;

import arduino.Arduino;
import com.example.springdevice.SpringDeviceApplication;
import com.example.springdevice.main.MyClient;
import java.io.IOException;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.*;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.websocket.Session;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;


/**
 *
 */
@Service
public class ArduinoService implements ArduinoConnect {
    MyClient myClient;
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

    public ArduinoService(){
    }

    public void smartHouse() {
        AdruinoCon.openConnection();
        temp();

    }
    @Override
    public void temp(){
        //Vi ska testa denna
        //AdruinoCon.serialWrite(String.valueOf(this.commandsTemp));
        String duckit  = AdruinoCon.serialRead();
        //String fuckit = AdruinoCon.serialRead(10); denna funkar för få det till java. Ska testa de ovanför om de funkar
        for (int j = 0; j < duckit.length(); ++j) {
            System.out.print(duckit);
        }
        //System.out.println(fuckit);
        //myClient.sendMessage("temperature={'_id':'Livingroom Thermometer', 'device':'thermometer', 'status':'" + fuckit + "'}");
    }
    @Override
    public void ledOn() {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }

    @Override
    public void ledOff() {
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void ledInsideOn() {
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void ledInsideOff() throws InterruptedException {
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }
    @Override
    public void alarmOn(){
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void alarmOff() {
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fan(){
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanHigh[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanMedium(){
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanMedium[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanOff(){
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanOff[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void fanLow(){
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFanLow[commandIndex]); // pick a command from an array and send it to USB
    }

}
