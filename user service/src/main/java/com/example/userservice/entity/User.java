package com.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "login")})
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @JoinColumn(name = "user_role")
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> role = new HashSet<>();

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"login\":\"" + login + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"groupName\":\"" + groupName + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"surname\":\"" + surname + "\"" +
                '}';
    }

    public User(UserCredential credential) {
        this.login = credential.getLogin();
        this.password = credential.getPassword();
    }

    public User(UserBuilder builder) {
        this.login = builder.getLogin();
        this.password = builder.getPassword();
        this.groupName = builder.getGroupName();
        this.name = builder.getName();
        this.surname = builder.getSurname();
    }
}

