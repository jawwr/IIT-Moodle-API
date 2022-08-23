package com.example.eventservice.service;

import com.example.eventservice.entity.Event;

import java.util.List;

public interface EventService {
    List<Event> getEventsByGroupName(String group);
}
