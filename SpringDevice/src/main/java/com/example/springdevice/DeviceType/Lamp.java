package com.example.springdevice.DeviceType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Lamp {
    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private String baseAddress = "http://194.47.32.140:8080/";
    String deviceID;
    boolean on;

    public Lamp(String deviceID, boolean on) {
        this.deviceID = deviceID;
        this.on = on;
    }

    @Override
    public String toString() {
        return "Lamp{" +
                "deviceID='" + deviceID + '\'' +
                ", on=" + on +
                '}';
    }
    public String changeLampStatus(String ready, String status) throws IOException, InterruptedException, JSONException {
        String message = ready;

        JSONObject json = new JSONObject();
        json.put("_id",ready);
        json.put("status", status);
        message = json.toString();

        // add json header
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create(baseAddress + "sendConfirmation"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            return "ok";
        }else {
            return "not ok";
        }
    }
}