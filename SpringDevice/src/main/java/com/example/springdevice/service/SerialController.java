package com.example.springdevice.service;

import com.example.springdevice.main.MyClient;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialController implements SerialPortDataListener {
    MyClient myClient;
    SerialPort serialPort;
    @Override
    public int getListeningEvents() {
        return serialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }

        byte[] newData = new byte[serialPort.bytesAvailable()];
        int numread = serialPort.readBytes(newData,newData.length);
        String buffer = new String(newData,0,numread).trim();
        System.out.println(buffer);


        myClient.sendMessage("temperature={'_id':'Livingroom Thermometer',device:'thermometer','status':" + buffer + "}");
        System.out.println("DIN MAMMAS FITTA FUNKAR DU??????");
        System.out.println(buffer);
    }
}
