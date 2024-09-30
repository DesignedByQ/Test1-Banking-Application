package com.maxoptra.test1.controller;

import com.maxoptra.test1.model.BankingDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class DataReceiver {

    @PostMapping("/submitdata")
    public ResponseEntity<String> submit(@RequestBody BankingDetails userInput){

    }

    @GetMapping("/displaydata")
    public ResponseEntity<String> display(){

    }


}
