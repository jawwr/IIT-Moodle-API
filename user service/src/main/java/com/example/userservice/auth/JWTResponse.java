package com.example.userservice.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JWTResponse {
    private String token;
    private final String type = "Bearer";
}