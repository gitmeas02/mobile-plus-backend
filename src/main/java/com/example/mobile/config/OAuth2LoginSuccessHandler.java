package com.example.mobile.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.mobile.auth.dto.ApiResponse;
import com.example.mobile.auth.dto.AuthData;
import com.example.mobile.auth.dto.AuthSuccessData;
import com.example.mobile.auth.dto.UserData;
import com.example.mobile.auth.utils.JwtProperties;
import com.example.mobile.auth.utils.JwtUtil;
import com.example.mobile.roles.Role;
import com.example.mobile.roles.Scope;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final String frontendUrl;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, String frontendUrl, ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.frontendUrl = frontendUrl;
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract user information
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String oauthId = oauth2User.getName();
        
        // Determine provider from registration ID
        String provider = determineProvider(request);
        
        // Handle OAuth2 login directly
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        UserEntity user;
        if (existingUser.isPresent()) {
            // User exists, update OAuth info
            user = existingUser.get();
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
        } else {
            // Create new user from OAuth
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(name != null ? name.replaceAll("\\s+", "") : email.split("@")[0]);
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
            user.setRoles(Role.USER);
            user.setEnabled(true);
            user.setOtpVerified(true); // OAuth users are pre-verified
            user.setPassword(passwordEncoder.encode(generateRandomPassword())); // Set random password
        }

        user.setLastLogin(Instant.now());
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles(), user.getFullname(), user.isEnabled());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // Store refresh token in database
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationMs()));
        userRepository.save(user);

        // Log successful save
        System.out.println("OAuth2 login successful for user: " + user.getEmail() + ", refreshToken saved: " + (user.getRefreshToken() != null));

        // Create auth data
        AuthData authData = new AuthData(accessToken, refreshToken, 3600L); // 1 hour expiration

        ApiResponse<AuthData> apiResponse = ApiResponse.success("OAuth login successful", authData);
        
        // Return JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String determineProvider(HttpServletRequest request) {
        // Check the request URI for the registration ID
        String requestUri = request.getRequestURI();
        if (requestUri != null) {
            if (requestUri.contains("/google")) return "google";
            if (requestUri.contains("/github")) return "github";
            if (requestUri.contains("/telegram")) return "telegram";
        }
        
        // Fallback: check referer header
        String referer = request.getHeader("referer");
        if (referer != null) {
            if (referer.contains("google")) return "google";
            if (referer.contains("github")) return "github";
            if (referer.contains("telegram")) return "telegram";
        }
        return "unknown";
    }

    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString();
    }
}
