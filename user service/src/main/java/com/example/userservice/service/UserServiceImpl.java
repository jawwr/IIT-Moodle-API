package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public User getInfoAboutUser() {
//        var authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return repository.findByLogin(((UserDetailImpl)authUser).getLogin());
        return new User();//TODO возможно убрать
    }

    /**
     * Метод для получения данных пользователя по логину
     * @param login логин пользователя
     * @return {@link User}
     */
    @Override
    public User getUserByLogin(String login) {
        return repository.findByLogin(login);
    }
}
