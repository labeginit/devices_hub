package com.example.springdevice.main;


import arduino.Arduino;
import com.example.springdevice.DeviceType.SmartHouse;
import com.example.springdevice.service.ArduinoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;


@ClientEndpoint
public class MyClient {
    private static final Arduino AdruinoCon = new Arduino("COM3", 9600);
    SmartHouse smartHouse;
    Session userSession = null;
    private MessageHandler messageHandler;
    private ArduinoService arduinoService = new ArduinoService();

    public MyClient(URI endpointURI) throws InterruptedException {
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
    public void onOpen(Session userSession){
        this.userSession = userSession;
        arduinoService.smartHouse();



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

    public interface MessageHandler {
        void handleMessage(String message);
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
        System.out.println("The Payload " + payload);

        if (operation.equals("changeDeviceStatus2Device")) {

            System.out.println("first sout");
            Gson gson = new Gson();
            //sending false to us wont work sending null or anything else instead will work

            // System.out.println("LAMMMMMMMMMMMP");
            System.out.println("The Payload " + payload);
            JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
            String deviceId = String.valueOf(jsonObject.get("_id")).replace("\"", "");
            String deviceType = String.valueOf(jsonObject.get("deviceType")).replace("\"", "");
            String status = String.valueOf(jsonObject.get("option")).replace("\"", "");


            if (deviceId.equals("Outdoor lamp")) {
                System.out.println("i am in handle lamp call");
                handleLamp(deviceId, status);

            } else if (deviceId.equals("Indoor lamp")) {
                System.out.println("i am indoor");
                handleIndoor(deviceId, status);
            } else if (deviceType.equals("fan")) {
                handleFan(deviceId, status);

            } else if (deviceId.equals("alarm")) {
                handleAlarm(deviceId, status);
            } else if (deviceType.equals("thermometer")) {
                handleTemp(deviceId);

            }
        }
    }

    public void handleLamp(String deviceId, String status) throws InterruptedException, JSONException {
        System.out.println("i am in hanldelapm method");
        if (deviceId.equalsIgnoreCase("Outdoor lamp") && status.equalsIgnoreCase("true")) {
            arduinoService.ledOn();
            System.out.println("i am in true in method handlelamp");
            System.out.println("lamp is on");
            String lampId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", lampId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Outdoor lamp','device':'lamp','status':'true','result':'success'}");
            System.out.println(message);
        } else if (deviceId.equalsIgnoreCase("Outdoor lamp") && status.equalsIgnoreCase("false")) {
            System.out.println("i am in false in handlelamp");
            arduinoService.ledOff();
            String lampId = deviceId;
            String fakeState = "false";
            JSONObject json = new JSONObject();
            json.put("_id", deviceId);
            json.put("status", fakeState);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Outdoor lamp','device':'lamp','status':'false','result':'success'}");
            System.out.println("lamp is off" + message);


        }
    }

    public void handleAlarm(String deviceId, String status) throws InterruptedException, JSONException, IOException {
        if (deviceId.equalsIgnoreCase("alarm") && status.equalsIgnoreCase("true")) {
            System.out.println("i am in true in method handlealarm");
            arduinoService.alarmOn();
            System.out.println("alarm is on");
            String alarmId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", alarmId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'alarm','device':'alarm','status':'0','result':'success'}");
            System.out.println(message);
        } else if (deviceId.equals("alarm") && status.equalsIgnoreCase("false")) {
            arduinoService.alarmOff();
            System.out.println("alarm is off");
            String alarmId = deviceId;
            String newStatus = "false";
            JSONObject json = new JSONObject();
            json.put("_id", alarmId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'alarm','device':'alarm','status':'1','result':'success'}");
            System.out.println(message);

        }
    }

    public void handleTemp(String deviceId) throws InterruptedException {
        if (deviceId.equalsIgnoreCase("thermometer")) {
            arduinoService.temp();
            // sendMessage("temperature={'_id':'Livingroom Thermometer',devide:'thermometer','status':'19'}");//status ska bytas ut med det som arduino
            // skickar
        }
    }

    public void handleIndoor(String deviceId, String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Indoor lamp") && status.equalsIgnoreCase("on")) {
            System.out.println("i am in true in method handleIndoor");
            arduinoService.ledInsideOn();
            System.out.println("lampin is on");
            String lampIds = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", lampIds);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Indoor lamp','device':'lamp','status':'true','result':'success'}");
            System.out.println(message);
        } else if (deviceId.equals("Indoor lamp") && status.equalsIgnoreCase("off")) {
            arduinoService.ledInsideOff();
            System.out.println("lampin is off");
            String lampIds = deviceId;
            String newStatus = "false";
            JSONObject json = new JSONObject();
            json.put("_id", lampIds);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Indoor lamp','device':'lamp','status':'false','result':'success'}");
            System.out.println(message);

        }

    }

    public void handleFan(String deviceId, String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Bedroom Fan") && status.equalsIgnoreCase("2")) {
            System.out.println("i am in true in method handlefan");
            arduinoService.fan();
            System.out.println("fan is on");
            String fanId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", fanId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Bedroom Fan','device':'fan','status':'2','result':'success'}");
            System.out.println(message);
        }
    }
}

