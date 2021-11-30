package com.example.springdevice.controller;

import com.example.springdevice.service.ArduinoConnect;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/*
@RestController
public class ApiController {
private Client client;

    //<=======================================================================>
    private ArduinoConnect arduinoService;

    @Autowired
    public ApiController(ArduinoConnect arduinoService) {
        this.arduinoService = arduinoService;
    }//<=======================================================================>


    @PostMapping(value = "/ledOn")
    public ResponseEntity<String> ledOn() throws InterruptedException {
        arduinoService.ledOn();
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping (value = "/updateLamp", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updateLamp(@RequestBody Lamp lamp) throws InterruptedException {
        arduinoService.ledOn();
        return "ok";
    }
    @PostMapping(value = "/changeDeviceStatus", headers = "Accept=", produces = "application/json", consumes = "application/json")
    public Object postResponse(@RequestBody String keyword) throws InterruptedException, JSONException, IOException {
        System.out.println("in postresponse");
        JsonObject jsonObject = new JsonParser().parse(keyword).getAsJsonObject();

        String deviceId = String.valueOf(jsonObject.get("_id")).replace("\"", "");
        String deviceType = String.valueOf(jsonObject.get("deviceType")).replace("\"", "");
        String status = String.valueOf(jsonObject.get("on")).replace("\"", "");

        if (deviceType.equalsIgnoreCase("lamp")){
            if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("true")) {
                arduinoService.ledOn();
                System.out.println("lamp is on");
                Client client = new Client();
                client.changeLampStatus("Outdoor lamp","true","lamp");
                return "Lamp On";

            } else if (deviceId.equals("Outdoor lamp") && status.equalsIgnoreCase("false"))
                arduinoService.ledOff();
            System.out.println("lamp is off");
            Client client = new Client();
            client.changeLampStatus("Outdoor lamp","false","lamp");
            return "Lamp Off";
        }

return "something went wrong";
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT, headers = "Accept=", produces = "application/json", consumes = "application/json")
    public String changeStatus( String deviceId,String status) throws IOException, InterruptedException, JSONException {
        // We can change them from the input from the Unit group.

        deviceId = "1";
        status = "on";
        client = new Client();
        client.changeLampStatus("1", "on","lamp");
        String result = client.changeLampStatus("1", "on","lamp");
        return result;


    }*/
//    @RequestMapping(value = "/import", method = RequestMethod.POST)
//    public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        String name = multipartFile.getOriginalFilename();
//        System.out.println("File name: "+name);
//        byte[] bytes = multipartFile.getBytes();
//        System.out.println("File uploaded content:\n" + new String(bytes));
//        return "file uploaded";
//    }
    //}



