package com.example.markservice.controller;

import com.example.markservice.exception.UserDoesNotExistException;
import com.example.markservice.service.MarkService;
import com.example.markservice.service.MarkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/marks")
public class MarkController {
    private final MarkService service;

    @Autowired
    public MarkController(MarkServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getMarks(@RequestBody Map<String, String> credentials){
        try{
            return ResponseEntity.ok(service.getMarks(credentials.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
