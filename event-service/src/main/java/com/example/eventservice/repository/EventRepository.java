package com.example.eventservice.repository;

import com.example.eventservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByGroupName(String groupName);
    @Query(value = "SELECT DISTINCT group_name FROM events", nativeQuery = true)
    Set<String> findAllGroupName();
}