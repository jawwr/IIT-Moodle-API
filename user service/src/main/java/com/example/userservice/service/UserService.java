package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;

public interface UserService {
    User login(UserCredential credential);
}
