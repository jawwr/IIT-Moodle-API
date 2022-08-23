package com.example.userservice.controller;

import com.example.userservice.entity.UserCredential;
import com.example.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authUser(@RequestBody UserCredential credential) {
        var response = service.signIn(credential);
        return ResponseEntity.ok(response);
    }
}
