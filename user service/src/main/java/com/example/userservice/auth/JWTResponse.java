package com.example.userservice.auth;

import com.example.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class JWTResponse {
    private String token;
    private final String type = "Bearer";
    private Long id;
    private String login;
    private String userName;
    private List<String> roles;
}