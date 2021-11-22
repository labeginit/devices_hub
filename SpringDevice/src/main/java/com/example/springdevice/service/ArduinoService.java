package com.example.springdevice.service;

import arduino.Arduino;
import com.example.springdevice.SpringDeviceApplication;
import org.springframework.stereotype.Service;

@Service
public class ArduinoService implements ArduinoConnect{
    SpringDeviceApplication one = new SpringDeviceApplication();
    private static final Arduino AdruinoCon = new Arduino("COM3", 9600);
    private boolean isOn; // state of the led
    String[] commands = {"off", "on"}; // commands that adruino can recognize
    @Override
    public void ledOn(){
        AdruinoCon.openConnection(); //open connection

        this.isOn = true; // if the current state is TRUE we set it to FALSE
        int commandIndex = 1; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }


    @Override
    public void ledOff() {
//        AdruinoCon.openConnection();
        this.isOn = false; // if the current state is TRUE we set it to FALSE
        int commandIndex =  0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
    }
}
