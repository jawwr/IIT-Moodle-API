package com.example.eventservice.controller;

import com.example.eventservice.entity.Event;
import com.example.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/{groupName}")
    public List<Event> getEventsByGroup(@PathVariable("groupName") String group, @RequestBody String login){
        return service.getEventsByGroupName(group);
    }
}