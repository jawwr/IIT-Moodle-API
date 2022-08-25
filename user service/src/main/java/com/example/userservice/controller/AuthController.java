package com.example.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для входа в систему
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

//    private final AuthService service;
//
//    @Autowired
//    public AuthController(AuthService service) {
//        this.service = service;
//    }
//    //аунтификация  пользователя
//    @PostMapping("/signIn")
//    public ResponseEntity<?> authUser(@RequestBody UserCredential credential) {
//        var response = service.signIn(credential);
//        return ResponseEntity.ok(response);
//    }
}
