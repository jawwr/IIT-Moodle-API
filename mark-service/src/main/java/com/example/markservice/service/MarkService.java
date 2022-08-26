package com.example.markservice.service;

import com.example.markservice.exception.UserDoesNotExistException;

public interface MarkService {
    Object getMarks(String login) throws UserDoesNotExistException;
}
