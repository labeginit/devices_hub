package com.example.springdevice.DeviceType;

public class Alarm {

    private String _id;
    private int status;

    public Alarm(String deviceID, int status) {
        this._id = deviceID;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "deviceID='" + _id + '\'' +
                ", status=" + status +
                '}';
    }
}
