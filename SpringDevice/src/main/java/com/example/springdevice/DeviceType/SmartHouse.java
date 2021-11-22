package com.example.springdevice.DeviceType;

import java.util.ArrayList;

public class SmartHouse { private static SmartHouse smartHouse = null;

    ArrayList<Lamp> lampList;
    ArrayList<Fan> fanList;
    ArrayList<Alarm> alarmList;
    ArrayList<Thermometer> temperatureSensorList;

    public void clear() {
        lampList.clear();
        fanList.clear();
        alarmList.clear();
        temperatureSensorList.clear();
    }


    private SmartHouse() {
        lampList = new ArrayList<>();
        fanList = new ArrayList<>();
        alarmList = new ArrayList<>();
        temperatureSensorList = new ArrayList<>();
    }

    public static SmartHouse getInstance() {
        if (smartHouse == null) {
            smartHouse = new SmartHouse();
        }
        return smartHouse;
    }

    public ArrayList<Lamp> getLampList() {
        return lampList;
    }

    public ArrayList<Fan> getFanList() {
        return fanList;
    }

    public ArrayList<Alarm> getAlarmList() {
        return alarmList;
    }

    public ArrayList<Thermometer> getTemperatureSensorList() {
        return temperatureSensorList;
    }

    public void addLamp(Lamp lamp) {
        this.lampList.add(lamp);
    }

    public void addFan(Fan fan) {
        this.fanList.add(fan);
    }

    public void addAlarm(Alarm alarm) {
        this.alarmList.add(alarm);
    }

    public void addTemperatureSensor(Thermometer temperatureSensor) {
        this.temperatureSensorList.add(temperatureSensor);
    }

    @Override
    public String toString() {
        return "SmartHouse{" +
                "lampList=" + lampList.toString() +
                ", fanList=" + fanList +
                ", AlarmList=" + alarmList +
                ", temperatureSensorList=" + temperatureSensorList +
                '}';
    }
}
