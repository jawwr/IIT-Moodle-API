package com.example.userservice.exceptions;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException() {
        super("Пользователя с такими данными не существует");
    }
}
