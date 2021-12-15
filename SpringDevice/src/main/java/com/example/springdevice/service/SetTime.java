//package com.example.springdevice.service;
//
//import com.example.springdevice.main.ClientStart;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//
//public class SetTime {
//    public static void SetTime() {
//
//        String timeStamp = "T" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        ClientStart.firstAvailableComPort.writeBytes(timeStamp.getBytes(), timeStamp.length());
//        System.out.println("Time set to: " + timeStamp);
//    }
//}
