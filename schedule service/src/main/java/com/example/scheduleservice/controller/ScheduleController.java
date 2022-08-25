package com.example.scheduleservice.controller;

import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.exceptions.UserDoesNotExistException;
import com.example.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService service;

    @Autowired
    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<?> getScheduleByGroupName(@RequestBody Map<String, String> login){
        try{
            return ResponseEntity.ok(service.getScheduleByGroupName(login.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public void createSchedule(@RequestBody ScheduleDTO schedule){
        service.createSchedule(schedule);
    }
}
