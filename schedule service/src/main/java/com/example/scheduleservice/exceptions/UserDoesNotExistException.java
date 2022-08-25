package com.example.scheduleservice.exceptions;

/**
 * Исключение в случае, если пользователя не существует
 */
public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException() {
        super("Пользователя с таким логином не существует");
    }
}
