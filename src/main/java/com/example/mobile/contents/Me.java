package com.example.mobile.contents;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Me {
    @GetMapping("/me")
    public String me() {
        return "This is the /me endpoint, accessible only to authenticated users.";
    }
}
