package com.example.userservice.repository;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    Boolean existsByLogin(String login);

//    @Query(value = "INSERT INTO users(login, password) VALUES (:#{#credential.login}, :#{#credential.password})", nativeQuery = true)
//    void saveCredential(@Param("credential") UserCredential credential);
}
