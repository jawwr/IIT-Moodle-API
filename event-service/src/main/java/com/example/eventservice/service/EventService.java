package com.example.eventservice.service;

import com.example.eventservice.entity.Event;
import com.example.eventservice.exceptions.UserDoesNotExistException;

import java.util.List;

public interface EventService {
    List<Event> getEvents(String group) throws UserDoesNotExistException;
}
