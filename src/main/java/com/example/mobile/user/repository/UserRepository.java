package com.example.mobile.user.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mobile.user.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, String> { // used when checking existence of users by id, username, email
    boolean existsByEmail(String email); // check if email exists
    boolean existsByUsername(String username); // check if username exists
    Optional<UserEntity> findByEmail(String email); // find user by email
    Optional<UserEntity> findByUsername(String username); // find user by username
    Optional<UserEntity> findByOauthProviderAndOauthId(String oauthProvider, String oauthId); // find by OAuth
}
