package com.example.scheduleservice.exceptions;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException() {
        super("Пользователя с таким логином не существует");
    }
}
