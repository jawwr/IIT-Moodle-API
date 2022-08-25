package com.example.scheduleservice.controller;

import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{groupName}")
    public ScheduleDTO getScheduleByGroupName(@PathVariable("groupName") String groupName){
        return service.getScheduleByGroupName(groupName);
    }

    @PostMapping("/create")
    public void createSchedule(@RequestBody ScheduleDTO schedule){
        service.createSchedule(schedule);
    }

    @GetMapping("/test")
    public Map<String, String> test(@RequestBody Map<String, String> login){
        return service.test(login.get("login"));
    }


//    @PutMapping("/update")
//    public void updateSchedule(@RequestBody Schedule schedule){
//        service.updateSchedule(schedule);
//    }
}
