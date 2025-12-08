package com.example.mobile.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.model.UserModel;
import com.example.mobile.user.repository.UserRepository;

@Service
public class UserService {
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
}
