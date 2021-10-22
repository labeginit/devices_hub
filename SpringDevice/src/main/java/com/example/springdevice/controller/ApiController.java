package com.example.springdevice.controller;

import com.example.springdevice.service.ArduinoConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

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


    @PostMapping(value = "/ledOff")
    public ResponseEntity<String> ledOff() {
        arduinoService.ledOff();
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
}
