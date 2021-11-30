package com.example.springdevice.DeviceType;

public class Alarm {

    String deviceID;
    boolean open;

    public Alarm(String deviceID, boolean open) {
        this.deviceID = deviceID;
        this.open = open;
    }
}
