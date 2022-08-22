package com.example.userservice.service;

import com.example.userservice.auth.JWTResponse;
import com.example.userservice.auth.JWTUtils;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            credential.getLogin(),
                            credential.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new JWTResponse(jwt);
        } catch (Exception e) {
            userRepository.save(new User(credential));
            return signUp(credential);
        }
    }

    private JWTResponse signUp(UserCredential credential){
        try {
            template.convertAndSend("myQueue", credential.toString());
        }catch (Exception e){
            System.err.println(e);
        }
        return signIn(credential);
    }
}
