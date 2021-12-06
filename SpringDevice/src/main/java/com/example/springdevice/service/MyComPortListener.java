package com.example.springdevice.service;

import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
//this is a part of timestamp,reformat,settime 
public class MyComPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return 0;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] buffer = new byte[serialPortEvent.getSerialPort().bytesAvailable()];
        serialPortEvent.getSerialPort().readBytes(buffer, buffer.length);

        ReformatBuffer.parseByteArray(buffer);
    }
}
