package com.example.userservice.service;

import com.example.userservice.auth.AuthTokenFilter;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<? extends GrantedAuthority> getUserRole(){
        var a = SecurityContextHolder.getContext().getAuthentication();
        return a.getAuthorities().stream().collect(Collectors.toList());
    }


    public User login(UserCredential credential) {
        return repository.findByLogin(credential.getLogin());
    }
}
