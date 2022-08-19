package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserService {
    User login(UserCredential credential);
    List<? extends GrantedAuthority> getUserRole();
}
