package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserBuilder;
import com.example.userservice.entity.UserCredential;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthService {
    private final UserRepository repository;
    private final AmqpTemplate template;

    @Autowired
    public AuthService(UserRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    /**
     * Аутентификация пользователя
     * @param credential
     * @return {@link String}
     * @throws UserDoesNotExistException
     */
    public String signIn(UserCredential credential) throws UserDoesNotExistException {
        if (repository.existsByLogin(credential.getLogin())){
            return "Authentication was successful";
        }
        var userInfo = parseUser(credential);
        repository.save(userInfo);
        return "Authentication was successful";
    }

    /**
     * Метод для парсинга пользователя если его нет в базе данных
     * @param credential
     * @return {@link User}
     * @throws UserDoesNotExistException
     */
    private User parseUser(UserCredential credential) throws UserDoesNotExistException {
        template.convertAndSend("user_parser_exchange", "user_parser_key", credential);
        try {
            ObjectMapper mapper = new ObjectMapper();
            var receiveMessage = template.receiveAndConvert("userQueueService", 10000L);
            if (receiveMessage == null){
                throw new UserDoesNotExistException();
            }
            User user;
            if (receiveMessage instanceof Map){
                Map<String, String> message = (Map<String, String>) receiveMessage;
                user = new UserBuilder()
                        .login(message.get("login"))
                        .password(message.get("password"))
                        .name(message.get("name"))
                        .surname(message.get("surname"))
                        .groupName(message.get("groupName"))
                        .build();
            }else {
                user = mapper.readValue(receiveMessage.toString(), User.class);
            }
            return user;
        }catch (JsonProcessingException e){
            log.info(e.getMessage());
        }
        throw new UserDoesNotExistException();
    }
}
