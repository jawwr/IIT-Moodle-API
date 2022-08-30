package com.example.userservice.controller;

import com.example.userservice.entity.UserCredential;
import com.example.userservice.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для входа в систему
 */
@RestController
@RequestMapping("/api/auth")
@Api(value = "Authentication controller", description = "Authentication controller")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    //аунтификация  пользователя
    @PostMapping("/signIn")
    @ApiOperation("Entry to the system")
    public ResponseEntity<?> authUser(@RequestBody UserCredential credential) {
        try {
            var response = service.signIn(credential);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
