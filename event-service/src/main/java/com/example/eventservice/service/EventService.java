package com.example.eventservice.service;

import com.example.eventservice.entity.Event;

import java.io.IOException;
import java.util.List;

public interface EventService {
    List<Event> getEventsByGroupName(String group);
}
