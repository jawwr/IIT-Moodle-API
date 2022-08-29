package com.example.userservice.service;

import com.example.userservice.entity.User;

public interface UserService {

    User getUserByLogin(String login);
}
