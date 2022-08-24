package com.example.userservice.service;

import com.example.userservice.auth.JWTResponse;
import com.example.userservice.auth.JWTUtils;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//@Service
public class AuthService {

    private final AmqpTemplate template;
    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    RoleRepository roleRepository;

    JWTUtils jwtUtils;

    @Autowired
    public AuthService(AmqpTemplate template, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, JWTUtils jwtUtils) {
        this.template = template;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    public JWTResponse signIn(UserCredential credential) {
        if (!userRepository.existsByLogin(credential.getLogin())){
            signUp(credential);
        }
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            credential.getLogin(),
                            credential.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return new JWTResponse(jwt);
    }

    private void signUp(UserCredential credential){
        try {
            template.convertAndSend("myQueue", credential.toString());
            Thread.sleep(5000L);
        }catch (Exception e){
            System.err.println(e);
        }
    }
}
