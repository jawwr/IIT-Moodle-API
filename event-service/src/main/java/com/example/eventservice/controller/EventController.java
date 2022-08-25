package com.example.eventservice.controller;

import com.example.eventservice.entity.Event;
import com.example.eventservice.exceptions.UserDoesNotExistException;
import com.example.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ККотроллер для работы с событиями
 */
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    /**
     * Метод для получения события
     * @param login
     * @return {@link List<Event>}
     */
    @GetMapping("/")
    public ResponseEntity<?> getEvents(@RequestBody Map<String, String> login){
        try {
            return ResponseEntity.ok(service.getEvents(login.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
