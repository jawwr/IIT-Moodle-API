package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.service.UserService;
import com.example.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с пользоватеелями
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    /**
     * Получение информации о пользователе
     * @return {@link User}
     */
    @GetMapping("/me")
    public ResponseEntity<?> login(@RequestBody Map<String, String> login){
        try{
            return ResponseEntity.ok(service.getUserInfoByLogin(login.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.notFound().build();
        }
    }
}
