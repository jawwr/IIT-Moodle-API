package com.example.scheduleservice.repository;

import com.example.scheduleservice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Schedule findByGroupName(String groupName);
}
