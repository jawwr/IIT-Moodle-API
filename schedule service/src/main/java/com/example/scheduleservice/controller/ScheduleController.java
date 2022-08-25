package com.example.scheduleservice.controller;

import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.exceptions.UserDoesNotExistException;
import com.example.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для работы с расписанием
 */
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService service;

    @Autowired
    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    /**
     * Метод получения расписания по логиу
     * @param login
     * @return {@link ScheduleDTO}
     */
    @GetMapping()
    public ResponseEntity<?> getScheduleByGroupName(@RequestBody Map<String, String> login){
        try{
            return ResponseEntity.ok(service.getSchedule(login.get("login")));
        }catch (UserDoesNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")//TODO сделать проверку на админа либо выключить после введения всех данных
    public void createSchedule(@RequestBody ScheduleDTO schedule){
        service.createSchedule(schedule);
    }
}
