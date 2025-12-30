package com.example.mobile.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mobile.auth.dto.ApiResponse;
import com.example.mobile.auth.dto.AuthData;
import com.example.mobile.auth.dto.AuthResponse;
import com.example.mobile.auth.dto.AuthSuccessData;
import com.example.mobile.auth.dto.LoginRequest;
import com.example.mobile.auth.dto.RegisterRequest;
import com.example.mobile.auth.dto.UserData;
import com.example.mobile.auth.dto.VerifyOTPRequest;
import com.example.mobile.auth.utils.JwtProperties;
import com.example.mobile.auth.utils.JwtUtil;
import com.example.mobile.roles.Role;
import com.example.mobile.roles.Scope;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TelegramOTPService telegramOTPService;

    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MINUTES = 5;

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // Create new user
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setTelegramChatId(request.getTelegramChatId());
        user.setOtpDeliveryMethod(request.getOtpDeliveryMethod());
        user.setRoles(Role.USER);
        user.setEnabled(true);
        user.setOauthProvider("local"); // Local registration

        // Generate and send OTP
        String otpCode = generateOTP();
        user.setOtpCode(otpCode);
        user.setOtpExpiry(Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60));
        user.setOtpVerified(false);

        userRepository.save(user);

        // Send OTP based on preference
        sendOTP(user, otpCode);

        AuthResponse response = new AuthResponse();
        response.setMessage("Registration successful! Please verify your OTP sent to your " + 
                           request.getOtpDeliveryMethod());
        response.setRequiresOtp(true);
        response.setEmail(user.getEmail());

        return response;
    }

    /**
     * Login with email/username and password
     */
    public AuthResponse login(LoginRequest request) {
        // Find user by email or username
        String identifier = request.getEmail() != null ? request.getEmail() : request.getUsername();
        
        UserEntity user = userRepository.findByEmail(identifier)
            .or(() -> userRepository.findByUsername(identifier))
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        // Generate and send OTP
        String otpCode = generateOTP();
        user.setOtpCode(otpCode);
        user.setOtpExpiry(Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60));
        user.setOtpVerified(false);
        userRepository.save(user);

        // Send OTP
        sendOTP(user, otpCode);

        AuthResponse response = new AuthResponse();
        response.setMessage("OTP sent to your " + user.getOtpDeliveryMethod() + ". Please verify to complete login.");
        response.setRequiresOtp(true);
        response.setEmail(user.getEmail());

        return response;
    }

    /**
     * Verify OTP and generate JWT token
     */
    public ApiResponse<AuthData> verifyOTP(VerifyOTPRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if OTP is valid
        if (user.getOtpCode() == null || !user.getOtpCode().equals(request.getOtpCode())) {
            throw new RuntimeException("Invalid OTP code");
        }

        // Check if OTP is expired
        if (user.getOtpExpiry() == null || Instant.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("OTP code has expired");
        }

        // Mark OTP as verified
        user.setOtpVerified(true);
        user.setOtpCode(null); // Clear OTP after verification
        user.setOtpExpiry(null);
        user.setLastLogin(Instant.now());
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles(), user.getFullname(), user.isEnabled());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // Store refresh token in database
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationMs()));
        userRepository.save(user);

        // Create auth data
        AuthData authData = new AuthData(accessToken, refreshToken, jwtProperties.getAccessTokenExpirationMs() / 1000);

        return ApiResponse.success("Authentication successful", authData);
    }

    /**
     * Resend OTP
     */
    public AuthResponse resendOTP(String email, String deliveryMethod) {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate new OTP
        String otpCode = generateOTP();
        user.setOtpCode(otpCode);
        user.setOtpExpiry(Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60));
        
        // Update delivery method if specified
        if (deliveryMethod != null && !deliveryMethod.isEmpty()) {
            user.setOtpDeliveryMethod(deliveryMethod);
        }
        
        userRepository.save(user);

        // Send OTP
        sendOTP(user, otpCode);

        AuthResponse response = new AuthResponse();
        response.setMessage("New OTP sent to your " + user.getOtpDeliveryMethod());
        response.setRequiresOtp(true);

        return response;
    }

    /**
     * Handle OAuth2 login (Google, GitHub, Telegram)
     */
    public ApiResponse<AuthData> handleOAuth2Login(String email, String username, String oauthProvider, String oauthId) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        UserEntity user;
        if (existingUser.isPresent()) {
            // User exists, update OAuth info
            user = existingUser.get();
            user.setOauthProvider(oauthProvider);
            user.setOauthId(oauthId);
        } else {
            // Create new user from OAuth
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(username != null ? username : email.split("@")[0]);
            user.setOauthProvider(oauthProvider);
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

        // Create auth data
        AuthData authData = new AuthData(accessToken, refreshToken, jwtProperties.getAccessTokenExpirationMs() / 1000);

        return ApiResponse.success("OAuth login successful", authData);
    }

    /**
     * Generate random OTP code
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    /**
     * Send OTP based on user's delivery preference
     */
    private void sendOTP(UserEntity user, String otpCode) {
        String deliveryMethod = user.getOtpDeliveryMethod() != null ? user.getOtpDeliveryMethod() : "email";

        if ("telegram".equalsIgnoreCase(deliveryMethod)) {
            if (user.getTelegramChatId() != null && !user.getTelegramChatId().isEmpty()) {
                telegramOTPService.sendOTPViaTelegram(user.getTelegramChatId(), otpCode, user.getUsername());
            } else {
                // Fallback to email if Telegram chat ID not set
                emailService.sendOTPEmail(user.getEmail(), otpCode, user.getUsername());
            }
        } else {
            // Default to email
            emailService.sendOTPEmail(user.getEmail(), otpCode, user.getUsername());
        }
    }

    /**
     * Refresh access token using refresh token
     */
    public ApiResponse<AuthData> refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        // Extract user ID from refresh token
        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        // Find user
        UserEntity user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        // Verify the refresh token matches the stored one
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Check if stored refresh token is expired
        if (user.getRefreshTokenExpiry() == null || Instant.now().isAfter(user.getRefreshTokenExpiry())) {
            throw new RuntimeException("Refresh token expired");
        }

        // Generate new tokens
        String newAccessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getUsername(), user.getEmail(), user.getRoles(), user.getFullname(), user.isEnabled());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // Update stored refresh token
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationMs()));
        userRepository.save(user);

        // Create auth data
        AuthData authData = new AuthData(newAccessToken, newRefreshToken, jwtProperties.getAccessTokenExpirationMs() / 1000);

        return ApiResponse.success("Token refreshed successfully", authData);
    }

    /**
     * Generate random password for OAuth users
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
