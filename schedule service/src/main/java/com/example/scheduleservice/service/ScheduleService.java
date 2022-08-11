package com.example.scheduleservice.service;

import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.entity.Schedule;
import com.example.scheduleservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository repository;

    @Autowired
    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    public ScheduleDTO getScheduleByGroupName(String groupName) {
        Schedule schedule = repository.findByGroupName(groupName);
        return new ScheduleDTO(schedule);
    }

    public void createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO);
        repository.save(schedule);
    }
}
