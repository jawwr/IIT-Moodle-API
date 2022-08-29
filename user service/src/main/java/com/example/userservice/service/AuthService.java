package com.example.userservice.service;

import com.example.userservice.config.RabbitConfig;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final AmqpTemplate template;

    @Autowired
    public AuthService(UserRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    public String signIn(UserCredential credential) throws UserDoesNotExistException {
        if (repository.existsByLogin(credential.getLogin())){
            return "Success";
        }
        var userInfo = parseUser(credential);
        repository.save(userInfo);
        return "Success";
    }
    private User parseUser(UserCredential credential) throws UserDoesNotExistException {
        template.convertAndSend("userQueueParser", "user_parser_exchange", credential);
        try {
            ObjectMapper mapper = new ObjectMapper();
            var receiveMessage = template.receiveAndConvert(RabbitConfig.QUEUE_NAME, 10000L);
            var user = mapper.readValue(receiveMessage.toString(), User.class);
            return user;
        }catch (Exception e){
            e.printStackTrace();//TODO добавить логгер
        }
        throw new UserDoesNotExistException();
    }
}
