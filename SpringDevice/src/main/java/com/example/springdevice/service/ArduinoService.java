package com.example.springdevice.service;

import arduino.Arduino;
import com.example.springdevice.SpringDeviceApplication;
import com.example.springdevice.main.MyClient;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.*;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;


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


    private boolean isOn; // state of the led
    String[] commands = {"off", "on"}; // commands that adruino can recognize
    String[] commandsInside = {"insideOff", "insideOn"};
    String[] commandsAlarm = {"alarmOn", "alarmOff"};
    String[] commandsTemp = {"temperature"};
    String[] commandsFan = {"fan"};

    public void smartHouse() {
        AdruinoCon.openConnection();
    }

    @Override
    public void ledOn() {
        // AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }


    @Override
    public void ledOff() {
        //AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
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
        // AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void recivietemp() {
        SerialPort port = SerialPort.getCommPort(portName);
        port.openPort();
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(newData, newData.length);
                if (numRead > 0) {
                    for (int i = 0; i < newData.length; ++i) {
                        if ((char) newData[i] == '\n' || (char) newData[i] == '\r') {
                            readline = readline.trim();
                            if (readline.length() > 0) receive(readline);
                            readline = "";
                        } else
                            readline = readline + (char) newData[i];
                    }
                }
            }
        });
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
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsFan[commandIndex]); // pick a command from an array and send it to USB
    }


    protected void receive(String line) {

        System.out.println(line);
        System.out.println(line);
        if (!readline.startsWith("thermometer")) {
            String[] words = readline.split(" ");
            String str = words[1].replaceAll("[^\\.0123456789]", ""); // remove all non-digits
            double temp = Double.parseDouble(str);
            if (temp > 15) {
                String formattedTemp = String.format(Locale.getDefault(), "%.1f", temp);
                myClient.sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + formattedTemp + "'}'");
                System.out.println(formattedTemp);

            }

        }
    }
}
