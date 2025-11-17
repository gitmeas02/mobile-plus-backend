package com.example.mobile.user.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mobile.user.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, String> { // used when checking existence of users by id, username, email
    boolean existsByEmail(String email); // check if email exists
}
