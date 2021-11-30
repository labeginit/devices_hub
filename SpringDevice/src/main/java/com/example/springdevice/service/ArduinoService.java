package com.example.springdevice.service;

import arduino.Arduino;
import com.example.springdevice.SpringDeviceApplication;
import org.springframework.stereotype.Service;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.InputStream;
import java.util.Enumeration;


@Service
public class ArduinoService implements ArduinoConnect, SerialPortEventListener {
    private byte[] readBuffer = new byte[400];
    private InputStream input;
    private String temperatureBuffer;
    SerialPort serialPort;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
    SpringDeviceApplication one = new SpringDeviceApplication();

    private static final String[] PORT_NAMES = {"COM3"};//ändra här om COM strular och säger tex COM3 istället för COM4
   private static final Arduino AdruinoCon = new Arduino("COM3", 9600);


    private boolean isOn; // state of the led
    String[] commands = {"off", "on"}; // commands that adruino can recognize
    String[] commandsInside = {"insideOff", "insideOn"};
    String[] commandsAlarm = {"alarmOn","alarmOff"};
    @Override
    public void ledOn(){
        AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }


    @Override
    public void ledOff() {
        AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex =  0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void ledInsideOn() {
        AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void ledInsideOff() throws InterruptedException {
        AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex =  0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsInside[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void recivietemp() throws InterruptedException {

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = serialPort.getInputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    @Override
    public void alarmOn() throws InterruptedException {
        AdruinoCon.openConnection(); //open connection
        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }

    @Override
    public void alarmOff() throws InterruptedException {
        AdruinoCon.openConnection(); //open connection
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex = 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commandsAlarm[commandIndex]); // pick a command from an array and send it to USB
    }


    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int available = input.available();
                byte chunk[] = new byte[available];
                input.read(chunk, 0, available);

                String s = new String(chunk);
                if(s.contains("T")) {
                    temperatureBuffer = s;
                } else {
                    temperatureBuffer += s;
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
