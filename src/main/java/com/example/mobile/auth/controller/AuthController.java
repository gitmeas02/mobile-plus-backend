package com.example.mobile.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobile.auth.dto.ApiResponse;
import com.example.mobile.auth.dto.AuthData;
import com.example.mobile.auth.dto.AuthResponse;
import com.example.mobile.auth.dto.LoginRequest;
import com.example.mobile.auth.dto.RefreshTokenRequest;
import com.example.mobile.auth.dto.RegisterRequest;
import com.example.mobile.auth.dto.ResendOTPRequest;
import com.example.mobile.auth.dto.VerifyOTPRequest;
import com.example.mobile.auth.service.AuthService;
import com.example.mobile.auth.utils.JwtProperties;
import com.example.mobile.auth.utils.JwtUtil;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Controller for handling authentication-related operations such as registration, login, OTP verification, etc.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
 * Registers a new user.
 * @param request the registration request
 * @return the authentication response
 */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Login with email/username and password
     * POST /auth/login
     * @param request the login request containing credentials
     * @return the authentication response with token or error
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Verify OTP code
     * POST /auth/verify-otp
     * @param request the OTP verification request
     * @return the authentication response
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthData>> verifyOTP(@Valid @RequestBody VerifyOTPRequest request) {
        try {
            ApiResponse<AuthData> response = authService.verifyOTP(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<AuthData> errorResponse = ApiResponse.error(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Refresh access token using refresh token
     * POST /auth/refresh
     * @param request the refresh token request
     * @return the authentication response with new tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthData>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            ApiResponse<AuthData> response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<AuthData> errorResponse = ApiResponse.error(401, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Resend OTP code
     * POST /auth/resend-otp
     * @param request the resend OTP request
     * @return the authentication response
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<AuthResponse> resendOTP(@Valid @RequestBody ResendOTPRequest request) {
        try {
            AuthResponse response = authService.resendOTP(request.getEmail(), request.getDeliveryMethod());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     * @return a string indicating the service status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth service is running");
    }

    /**
     * Test endpoint
     * @return a string confirming the endpoint is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working");
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Find user entity
        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate new tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles(), user.getFullname(), user.isEnabled());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());
        
        // Store refresh token in database
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(java.time.Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationMs()));
        userRepository.save(user);
        
        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId().toString());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setSuccess(true);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtProperties.getAccessTokenExpirationMs() / 1000); // in seconds
        
        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Clear the JWT cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .domain(".khunmeas.site")
            .maxAge(0)
            .sameSite("None")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
        
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Initiate Google OAuth2 login
     */
    @GetMapping("/google") // request http://localhost:8080/auth/google
    public ResponseEntity<String> googleLogin() {
        // Redirect to the OAuth2 authorization endpoint
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", "/oauth2/authorization/google")
            .build();
    }
}
