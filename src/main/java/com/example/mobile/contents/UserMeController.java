package com.example.mobile.contents;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobile.auth.dto.ApiResponse;
import com.example.mobile.auth.utils.JwtUtil;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserMeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        String token = authorizationHeader.substring(7);
        String userId = jwtUtil.getUserIdFromToken(token);

        UserEntity user = userRepository.findById(java.util.UUID.fromString(userId))
            .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId().toString());
        userData.put("username", user.getUsername());
        userData.put("fullName", user.getFullname());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRoles().name());
        userData.put("status", user.isEnabled() ? "ACTIVE" : "INACTIVE");

        return ApiResponse.success("User data retrieved", userData);
    }

    @GetMapping("/whoami")
    public String whoAmI() {
        return "You are authenticated!";
    }
}
