package com.example.springdevice.main;


import com.example.springdevice.DeviceType.Alarm;
import com.example.springdevice.DeviceType.Fan;
import com.example.springdevice.DeviceType.Lamp;
import com.example.springdevice.DeviceType.SmartHouse;
import com.example.springdevice.service.ArduinoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;


@ClientEndpoint
public class MyClient implements Runnable {
    public static MyClient myClient = new MyClient();
    Session userSession = null;
    private MessageHandler messageHandler;
    private ArduinoService arduinoService = new ArduinoService();

    private MyClient() {
    }

    public void start(URI endpointURI) {
        try {

            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
            container.connectToServer(this, endpointURI);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MyClient getInstance() {
        return myClient;
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {

        this.userSession = userSession;
            arduinoService.smartHouse();



    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) throws JSONException, InterruptedException, IOException {

        System.out.println("Connected with the server");
        readMessage(message);


    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    @Override
    public void run() {
        int temp = 20;
        Random brnbedzie = new Random();
        for (int i = 20; i < 24; i++) {
            System.out.println(brnbedzie.nextInt(24 - 20) + 20);
            temp = (brnbedzie.nextInt(24 - 20) + 20);
        }
        String bemp = String.valueOf(temp);
        sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + bemp + "'}");
        //rec(String.valueOf(temp));
//        Thread.currentThread().stop();
    }

    public void tempe(){
        int temp = 20;
        Random brnbedzie = new Random();
        for (int i = 20; i < 24; i++) {
            System.out.println(brnbedzie.nextInt(24 - 20) + 20);
            temp = (brnbedzie.nextInt(24 - 20) + 20);
        }
        String bemp = String.valueOf(temp);
        sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + bemp + "'}");
//            rec(String.valueOf(temp));

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

        //changeDeviceStatus ÄNDRA TILL DEN OM NÄR UNITS SKICKAR OCH DET INTE FUNKAR
        if (operation.equals("changeDeviceStatus2Device")) {

//            if (operation.equals("getDevices")) {
//                readGetAll(payload);
//            }
            System.out.println("first sout");
            Gson gson = new Gson();
            //sending false to us wont work sending null or anything else instead will work

            // System.out.println("LAMMMMMMMMMMMP");
            System.out.println("The Payload " + payload);
            JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
            String deviceId = String.valueOf(jsonObject.get("_id")).replace("\"", "");
            String deviceType = String.valueOf(jsonObject.get("deviceType")).replace("\"", "");
            String status = String.valueOf(jsonObject.get("status")).replace("\"", "");


            if (deviceId.equals("Outdoor lamp")) {
                System.out.println("i am in handle lamp call");
                handleLamp(deviceId, status);

            } else if (deviceId.equals("Indoor lamp")) {
                System.out.println("i am indoor");
                handleIndoor(deviceId, status);
            } else if (deviceId.equals("Bedroom Fan") && status.equalsIgnoreCase("3")) {
                handleFanHigh(deviceId, status);
            } else if (deviceId.equals("Bedroom Fan") && status.equalsIgnoreCase("2")) {
                handleFanMedium(deviceId, status);
            }else if (deviceId.equals("Bedroom Fan") && status.equalsIgnoreCase("1")) {
                handleFanLow(deviceId, status);
            }else if (deviceId.equals("Bedroom Fan") && status.equalsIgnoreCase("0")) {
                handleFanOff(deviceId, status);
            }
            else if (deviceId.equals("alarm")) {
                handleAlarm(deviceId, status);
            } else if (deviceId.equals("House Heater")) {
                handleHeater(deviceId, status);
            } else if (deviceId.equals("Livingroom Thermometer")) {
//                run();
            }
        }
    }

    public void handleLamp(String deviceId, String status) throws JSONException {
        System.out.println("i am in hanldelapm method");
        System.out.println(deviceId);
        System.out.println(status);
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
            run();
            System.out.println(message);
        } else if (deviceId.equalsIgnoreCase("Outdoor lamp") && status.equalsIgnoreCase("false")) {
            System.out.println(deviceId);
            System.out.println(status);
            System.out.println("i am in false in handlelamp");
            arduinoService.ledOff();
            String lampId = deviceId;
            String fakeState = "false";
            JSONObject json = new JSONObject();
            json.put("_id", deviceId);
            json.put("status", fakeState);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Outdoor lamp','device':'lamp','status':'false','result':'success'}");
            run();
            System.out.println("lamp is off" + message);


        }
    }

    public void handleAlarm(String deviceId, String status) throws JSONException, InterruptedException {
        if (deviceId.equalsIgnoreCase("alarm") && status.equalsIgnoreCase("1")) {
            System.out.println("i am in true in method handlealarm");
            arduinoService.alarmOn();
            System.out.println("alarm is on");
            String alarmId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", alarmId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'alarm','device':'alarm','status':'1','result':'success'}");
            System.out.println(message);
        } else if (deviceId.equals("alarm") && status.equalsIgnoreCase("0")) {
            arduinoService.alarmOff();
            System.out.println("alarm is off");
            String alarmId = deviceId;
            String newStatus = "false";
            JSONObject json = new JSONObject();
            json.put("_id", alarmId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'alarm','device':'alarm','status':'0','result':'success'}");
            System.out.println(message);

        }
    }

    public void handleTemp(String deviceId) {
        if (deviceId.equalsIgnoreCase("Livingroom Thermometer")) {
            tempe();
            // sendMessage("temperature={'_id':'Livingroom Thermometer',devide:'thermometer','status':'19'}");//status ska bytas ut med det som arduino
            // skickar
        }
    }

    public void handleIndoor(String deviceId, String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Indoor lamp") && status.equalsIgnoreCase("true")) {
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
            run();
        } else if (deviceId.equals("Indoor lamp") && status.equalsIgnoreCase("false")) {
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
            run();

        }

    }

    public void handleFanHigh(String deviceId, String status) throws JSONException, InterruptedException {
        if (deviceId.equalsIgnoreCase("Bedroom Fan") && status.equalsIgnoreCase("3")) {
            System.out.println("i am in true in method handlefan");
            arduinoService.fan();
            System.out.println("fan is on high");
            String fanId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", fanId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Bedroom Fan','device':'fan','status':'3','result':'success'}");
            System.out.println(message);
        }
    }

    public void handleFanMedium(String deviceId,String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Bedroom Fan") && status.equalsIgnoreCase("2")) {
            System.out.println("i am in true in method handlefan");
            arduinoService.fanMedium();
            System.out.println("fan is on medium");
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

    public void handleFanLow(String deviceId,String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Bedroom Fan") && status.equalsIgnoreCase("1")) {
            System.out.println("i am in true in method handlefan");
            arduinoService.fanLow();
            System.out.println("fan is on low");
            String fanId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", fanId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Bedroom Fan','device':'fan','status':'1','result':'success'}");
            System.out.println(message);
        }
    }

    public void handleFanOff(String deviceId,String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("Bedroom Fan") && status.equalsIgnoreCase("0")) {
            System.out.println("i am in true in method handlefan");
            arduinoService.fanOff();
            System.out.println("fan is off");
            String fanId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", fanId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'Bedroom Fan','device':'fan','status':'0','result':'success'}");
            System.out.println(message);
        }
    }

    public void handleHeater(String deviceId, String status) throws InterruptedException, JSONException {
        if (deviceId.equalsIgnoreCase("House Heater") && status.equalsIgnoreCase("true")) {
            System.out.println("i am in true in method handleHeater");
            arduinoService.heater();
            System.out.println("heaters is on");
            String heaterId = deviceId;
            String newStatus = "true";
            JSONObject json = new JSONObject();
            json.put("_id", heaterId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'House Heater','device':'heater','status':'true','result':'success'}");
            System.out.println(message);
        } else if (deviceId.equals("House Heater") && status.equalsIgnoreCase("false")) {
            System.out.println("i am in false in method handleHeater");
            arduinoService.heaterOff();
            System.out.println("heaters is off");
            String heaterId = deviceId;
            String newStatus = "false";
            JSONObject json = new JSONObject();
            json.put("_id", heaterId);
            json.put("status", newStatus);
            String message = json.toString();
            sendMessage("confirmation={'_id':'House Heater','device':'heater','status':'false','result':'success'}");
            System.out.println(message);
        }
    }

    public void rec(String temp) throws InterruptedException {
        sendMessage("temperature={'_id':'Livingroom Thermometer','device':'thermometer','status':'" + temp + "'}");
        System.out.println("sending TEMP!!!!");
//        Thread.sleep(30000);

    }
    public void readGetAll(String payLoad) throws JSONException, InterruptedException, IOException {
        Gson gson = new Gson();
        SmartHouse smartHouse = SmartHouse.getInstance();
        smartHouse.clear();

        System.out.println("New payload " + payLoad);

        smartHouse = gson.fromJson(payLoad, SmartHouse.class);

        System.out.println("New payloa2 "+ smartHouse);

        System.out.println("Lamp List " + smartHouse.getLampList());

        System.out.println("Fan List " + smartHouse.getFanList());

        System.out.println("Alarm List " + smartHouse.getAlarmList());




        ArrayList fanList = smartHouse.getFanList();

        ArrayList lampList = smartHouse.getLampList();

        ArrayList alarmList = smartHouse.getAlarmList();



        System.out.println("Here are all FANS" + fanList);
        for (int i = 0; i <fanList.size(); i++) {
            Fan fan = (Fan) fanList.get(i);
            System.out.println("Here is the faaaaaaan" + fan);

            String fanId= fan.get_id();
            System.out.println("fan id "+fanId);

            int fanStatus = fan.getStatus();
            System.out.println("fan Speed" +fanStatus);

//            handleFan(fanId,String.valueOf(fanStatus));

        }

        for (int i = 0; i < lampList.size(); i++) {

            Lamp lamp1 = (Lamp) lampList.get(i);

            String lampId= lamp1.get_id();

            boolean lamp1Status = lamp1.isStatus();

            handleLamp(lampId, String.valueOf(lamp1Status));

        }

        for (int i = 0; i <alarmList.size(); i++) {
            Alarm alarm = (Alarm) alarmList.get(i);
            System.out.println("Here is the alarm" + alarm);
String device = "temp";
            String alarId= alarm.get_id();
            System.out.println("Alaram id "+alarId);

            int alaramStaus = alarm.getStatus();
            System.out.println("Alarm Speed" +alaramStaus);

            handleAlarm(alarId,String.valueOf(alaramStaus));
handleTemp(device);
        }

    }
}

