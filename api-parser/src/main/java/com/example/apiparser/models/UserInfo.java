package com.example.apiparser.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String name;
    private String surname;
    private String groupName;
    private String login;
    private String password;
    @Override
    public String toString() {
        return "{" +
                "\"name\" : \"" + name + "\"," +
                "\"surname\" : \"" + surname + "\"," +
                "\"groupName\" : \"" + groupName + "\"," +
                "\"login\" : \"" + login + "\"," +
                "\"password\" : \"" + password + "\"" +
                '}';
    }
}
