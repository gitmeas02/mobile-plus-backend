package com.example.mobile.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.model.UserModel;
import com.example.mobile.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    // 
    @Autowired // used for dependency injection
    private UserRepository userRepository;
    // get all users 
    public List<UserModel> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserModel> userModels = new ArrayList<>();
        for (UserEntity entity : userEntities) {
            userModels.add(new UserModel(entity.getId(), entity.getUsername(), entity.getEmail()));
        }
        return userModels;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles() != null ? userEntity.getRoles().split(",") : new String[]{})
                .disabled(!userEntity.isEnabled())
                .build();
    }
}
