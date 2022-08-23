package com.example.eventservice.repository;

import com.example.eventservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events WHERE group_name=:#{#group}", nativeQuery = true)
    List<Event> findAllByGroupName(@Param("group") String group);
    @Query(value = "SELECT DISTINCT group_name FROM events", nativeQuery = true)
    Set<String> findAllGroupName();
    Boolean existsByGroupName(String groupName);
}