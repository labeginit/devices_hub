package com.example.springdevice.main;


import com.example.springdevice.DeviceType.Lamp;
import com.example.springdevice.DeviceType.SmartHouse;
import com.example.springdevice.service.ArduinoConnect;
import com.example.springdevice.service.ArduinoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.ClientEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Iterator;


@ClientEndpoint
public class MyClient {
    SmartHouse smartHouse;
    Session userSession = null;
    private MessageHandler messageHandler;
    private ArduinoConnect arduinoService = new ArduinoService();
    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private String baseAddress = "http://194.47.32.140:8080/";



    public MyClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
            container.connectToServer(this, endpointURI);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;

    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) throws JSONException, IOException, InterruptedException {

        System.out.println("Connected with the server");
        readMessage(message);

    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }

    public void readMessage(String message) throws JSONException, InterruptedException, IOException {
        //The smart house look like exactly the same for smartHouse class in the server side

        // Incoming message will be two parts as you can see first one is the operation and here it will be ("changeDeviceStatus") always, and the payLoad is the JSOn one
        // u will ignore
        System.out.println(message);
        String[] parts = message.split("=");
        String operation = parts[0];
        System.out.println("The operation " + operation);
        System.out.println();
        String payload = parts[1];
        System.out.println("The Payload "+ payload);

        if (operation.equals("changeDeviceStatus")) {
            Gson gson = new Gson();

            Lamp lamp = gson.fromJson(payload,Lamp.class);
            System.out.println("LAMMMMMMMMMMMP" + lamp);

            JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
            String deviceId = String.valueOf(jsonObject.get("_id")).replace("\"", "");
            String deviceType = String.valueOf(jsonObject.get("deviceType")).replace("\"", "");
            String status = String.valueOf(jsonObject.get("option")).replace("\"", "");


            if (deviceId.equals("Outdoor lamp")) {
                handleLamp(deviceId, status);
            } else if (deviceType.equals("fan")) {

            } else if (deviceType.equals("alarm")) {
                handleAlarm(deviceId,status);
            } else if (deviceType.equals("thermometer")) {
                handleTemp(status);
            }
        }
    }

public void handleAlarm(String deviceId, String status) throws InterruptedException, JSONException, IOException {
        if (status.equalsIgnoreCase("true")){
            arduinoService.alarmOn();
            String alarmId = deviceId;
            String newAlarmStatus = status;
            String message;
            if (newAlarmStatus.equalsIgnoreCase("true")) {
                newAlarmStatus = "true";
                JSONObject json = new JSONObject();
                json.put("_id",deviceId);
                json.put("status", newAlarmStatus);
                message = json.toString();

                // add json header
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                        .uri(URI.create(baseAddress + "sendConfirmation"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            }
        }else if (status.equalsIgnoreCase("false")){
            arduinoService.alarmOff();
            String alarmId = deviceId;
            String newAlarmStatus = status;
            String message;
            if (newAlarmStatus.equalsIgnoreCase("false")) {
                newAlarmStatus = "false";
                JSONObject json = new JSONObject();
                json.put("_id",deviceId);
                json.put("status", newAlarmStatus);
                message = json.toString();

                // add json header
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                        .uri(URI.create(baseAddress + "sendConfirmation"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            }
        }
}
    public void handleLamp(String deviceId, String status) throws InterruptedException, JSONException, IOException {

        if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("true")) {
            arduinoService.ledOn();
            System.out.println("lamp is on");
            String lampId = deviceId;
            String newLampStatus = status;
            String message;
            if (newLampStatus.equalsIgnoreCase("true")) {
                newLampStatus = "true";
                JSONObject json = new JSONObject();
                json.put("_id",deviceId);
                json.put("status", newLampStatus);
                message = json.toString();

                // add json header
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                        .uri(URI.create(baseAddress + "sendConfirmation"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            }

        } else if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("false")) {
            arduinoService.ledOff();
            System.out.println("lamp is off");
            String lampId = deviceId;
            String newLampStatus = status;
            String message;
            if (newLampStatus.equalsIgnoreCase("false")) {
                newLampStatus = "false";
                JSONObject json = new JSONObject();
                json.put("_id",deviceId);
                json.put("status", newLampStatus);
                message = json.toString();

                // add json header
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(message))
                        .uri(URI.create(baseAddress + "sendConfirmation"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            }
        } else if (deviceId.equals("inter lamp") && status.equalsIgnoreCase("true")) {
            arduinoService.ledInsideOn();
        } else if (deviceId.equals("inter lamp") && status.equalsIgnoreCase("false")) {
            arduinoService.ledInsideOff();
        }
    }

    public void handleTemp(String status) throws InterruptedException {
        if (status.equalsIgnoreCase("temperature")){
            arduinoService.recivietemp();
        }
    }


    }



