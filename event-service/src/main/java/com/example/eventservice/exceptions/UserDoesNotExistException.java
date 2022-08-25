package com.example.eventservice.exceptions;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException() {
        super("Пользователя с таким логином не существует");
    }
}
