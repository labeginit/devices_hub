package com.example.springdevice.DeviceType;

public class Fan {
    private String _id;
    private int status;


    public Fan(String deviceID, int speed) {
        this._id = deviceID;
        this.status = speed;
    }

    public String get_id() {
        return _id;
    }

    public int getStatus() {
        return status;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Fan{" +
                "_id='" + _id + '\'' +
                ", status=" + status +
                '}';
    }
}
