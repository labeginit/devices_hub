package com.example.springdevice.DeviceType;

public class Thermometer {
    double temperature;
    String deviceID;

    public Thermometer(String deviceID, double temperature) {
        this.deviceID = deviceID;
        this.temperature = temperature;
    }
}
