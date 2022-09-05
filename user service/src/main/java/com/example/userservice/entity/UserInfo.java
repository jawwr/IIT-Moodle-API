package com.example.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String groupName;

    private String name;

    private String surname;

    public UserInfo(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.groupName = user.getGroupName();
    }
}
