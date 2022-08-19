package com.example.userservice.controller;

import com.example.userservice.service.UserService;
import com.example.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

//    @GetMapping("/login")
//    public User login(@RequestBody UserCredential credential){
//        return service.login(credential);
//    }
    @GetMapping("/all")
    public String getAPI(){
        return "open API";
    }
    @GetMapping("/users")
    public List<? extends GrantedAuthority> getUserAPI(){
        return service.getUserRole();
    }

    @GetMapping("/users/Admin")
    public String getAdminAPI(){
        return "Admin API";
    }
}
