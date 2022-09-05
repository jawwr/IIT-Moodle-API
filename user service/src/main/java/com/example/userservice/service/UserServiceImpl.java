package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserInfo;
import com.example.userservice.exceptions.UserDoesNotExistException;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с пользоваттелями
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserInfo getUserInfoByLogin(String login) throws UserDoesNotExistException {
        User user = repository.findByLogin(login).orElse(new User());
        if (user.getLogin().equals("null")){
            throw new UserDoesNotExistException();
        }
        return new UserInfo(user);
    }

    /**
     * Метод для получения данных пользователя по логину
     * @param login логин пользователя
     * @return {@link User}
     */
    @Override
    public User getUserByLogin(String login) {
        return repository.findByLogin(login).orElse(new User());
    }


}
