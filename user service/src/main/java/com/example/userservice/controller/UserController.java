package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.service.UserService;
import com.example.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/login")
    public User login(@RequestBody UserCredential credential){
        return service.login(credential);
    }
}
