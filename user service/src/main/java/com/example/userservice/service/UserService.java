package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserInfo;
import com.example.userservice.exceptions.UserDoesNotExistException;

public interface UserService {
    UserInfo getUserInfoByLogin(String login) throws UserDoesNotExistException;

    User getUserByLogin(String login);
}
