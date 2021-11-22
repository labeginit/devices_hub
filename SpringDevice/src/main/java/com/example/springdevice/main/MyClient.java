package com.example.springdevice.main;

import com.example.springdevice.DeviceType.Lamp;
import com.example.springdevice.DeviceType.SmartHouse;
import com.example.springdevice.service.ArduinoConnect;
import com.example.springdevice.service.ArduinoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.ClientEndpoint;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class MyClient {
    SmartHouse smartHouse;
    Session userSession = null;
    private MessageHandler messageHandler;
    private ArduinoConnect arduinoService = new ArduinoService();


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

    public void readMessage(String message) throws JSONException, InterruptedException {
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

            } else if (deviceType.equals("curtain")) {

            } else if (deviceType.equals("thermometer")) {

        }
    }
}


    public void handleLamp(String deviceId, String status) throws InterruptedException {
        if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("true")) {
            arduinoService.ledOn();
            System.out.println("lamp is on");
        } else if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("false")) {
            arduinoService.ledOff();
        } else if (deviceId.equals("inter lamp") && status.equalsIgnoreCase("true")) {

        } else if (deviceId.equals("inter lamp") && status.equalsIgnoreCase("false")) {

        }
    }

}