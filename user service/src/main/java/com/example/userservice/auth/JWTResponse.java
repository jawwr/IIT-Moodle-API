package com.example.userservice.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JWTResponse {
    private String token;
    private final String type = "Bearer";
}