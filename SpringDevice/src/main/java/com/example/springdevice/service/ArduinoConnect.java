package com.example.springdevice.service;

public interface ArduinoConnect {
    void ledOn() throws InterruptedException;

    void ledOff() throws InterruptedException;

    void ledInsideOn() throws InterruptedException;

    void ledInsideOff() throws InterruptedException;

    void recivietemp() throws InterruptedException;

    void alarmOn() throws InterruptedException;

    void alarmOff() throws InterruptedException;

    void fan() throws InterruptedException;
}
