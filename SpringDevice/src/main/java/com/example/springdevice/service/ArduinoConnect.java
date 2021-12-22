package com.example.springdevice.service;

interface ArduinoConnect {
    void ledOn() throws InterruptedException;

    void temp();

    void ledOff() throws InterruptedException;

    void heater() throws InterruptedException;

    void heaterOff() throws InterruptedException;

    void ledInsideOn() throws InterruptedException;

    void ledInsideOff() throws InterruptedException;

    //void recivietemp() throws InterruptedException;

    void alarmOn() throws InterruptedException;

    void alarmOff() throws InterruptedException;

    void fan() throws InterruptedException;

    void fanMedium() throws InterruptedException;

    void fanOff() throws InterruptedException;

    void fanLow() throws InterruptedException;
}
