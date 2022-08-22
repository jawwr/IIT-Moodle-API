package com.example.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "login")})
public class User {
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
    private Set<Role> role = new HashSet<>();

    public User(UserCredential credential) {
        this.login = credential.getLogin();
        this.password = credential.getPassword();
    }
}
