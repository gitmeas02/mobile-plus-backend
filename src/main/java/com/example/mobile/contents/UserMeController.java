package com.example.mobile.contents;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserMeController {
    @GetMapping("/whoami")
    public String whoAmI() {
        return "You are authenticated!";
    }
}
