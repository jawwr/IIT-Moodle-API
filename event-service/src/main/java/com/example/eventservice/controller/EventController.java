package com.example.eventservice.controller;

import com.example.eventservice.entity.Event;
import com.example.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Event> getEventsByGroup(@RequestBody Map<String, String> login){
        return service.getEventsByGroupName(login.get("login"));
    }
}
