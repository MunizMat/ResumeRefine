package com.resumeassist.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class DefualtController {

    @GetMapping
    public ResponseEntity<String> getServiceStatus(){
        return ResponseEntity.ok("Ok");
    }
}
