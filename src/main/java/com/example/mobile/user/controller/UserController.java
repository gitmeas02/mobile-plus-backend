package com.example.mobile.user.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobile.user.model.UserModel;
import com.example.mobile.user.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService; // call service layer for business logic
    
    @GetMapping("/all")
    public Page<UserModel> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
       Pageable pageable = PageRequest.of(page,size);
        return userService.getAllUsers(pageable);
    } 
}
