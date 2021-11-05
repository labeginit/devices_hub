package com.example.springdevice.service;

public interface ArduinoConnect {
    void ledOn() throws InterruptedException;

    void ledOff() throws InterruptedException;
}
