package com.example.userservice.entity;

import lombok.Getter;

@Getter
public class UserBuilder {

    private String login;

    private String password;

    private String groupName;

    private String name;

    private String surname;

    public UserBuilder login(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    public User build() {
        return new User(this);
    }
}
