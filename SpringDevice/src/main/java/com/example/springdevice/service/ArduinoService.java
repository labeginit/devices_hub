package com.example.springdevice.service;

import arduino.Arduino;
import org.springframework.stereotype.Service;

@Service
public class ArduinoService implements ArduinoConnect{
    private static Arduino AdruinoCon = new Arduino("COM3", 9600);
    private boolean isOn = false; // state of the led
    String[] commands = {"turnOff", "turnOn"}; // commands that adruino can recognize
    @Override
    public void ledOn(){
        AdruinoCon.openConnection(); //open connection
        this.isOn = !this.isOn; // if the current state is TRUE we set it to FALSE
        int commandIndex = (this.isOn) ? 1 : 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB

    }


    @Override
    public void ledOff() {
        this.isOn = !this.isOn; // if the current state is TRUE we set it to FALSE
        int commandIndex = (this.isOn) ? 1 : 0; // false = 0; true = 1
        AdruinoCon.serialWrite(this.commands[commandIndex]); // pick a command from an array and send it to USB
    }
}
