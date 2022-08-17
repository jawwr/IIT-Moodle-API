package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User login(UserCredential credential) {
        return repository.findByLogin(credential.getLogin());
    }
}
