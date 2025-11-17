package com.example.mobile.user.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobile.user.dto.UserDTO;
import com.example.mobile.user.model.UserModel;
import com.example.mobile.user.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService; // call service layer for business logic
    
    @PostMapping("/create")
    public UserModel createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
   
    @GetMapping("/all")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }
    
}
