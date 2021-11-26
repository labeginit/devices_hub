package com.example.springdevice.controller;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Client {
    //server: 194.47.41.141
    //server port: 1337
    //private String baseAddress = "http://194.47.40.108:1337/";
    private String localBase = "http://localhost:1337/";

    //private WebTarget webTarget; //Will use it then

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();


    public String changeLampStatus(String deviceId, String status, String deviceType) throws IOException, InterruptedException, JSONException {
        String message = "";

        JSONObject json = new JSONObject();
        json.put("_id",deviceId);
        json.put("deviceType", deviceType);
        json.put("on", status);
        message = json.toString();

        // add json header
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create(localBase + "sendConfirmation2"))
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
