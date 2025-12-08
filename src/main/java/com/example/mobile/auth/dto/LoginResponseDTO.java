package com.example.mobile.auth.dto;
import java.time.Instant;

public class LoginResponseDTO {

    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String roles;
    private String token;           // JWT access token
    private String refreshToken;    // JWT refresh token
    private Instant refreshTokenExpiry;
    private Instant lastLogin;

    // Constructors
    public LoginResponseDTO() { }

    public LoginResponseDTO(String id, String username, String email, String phoneNumber,
                            String roles, String token, String refreshToken, Instant refreshTokenExpiry,
                            Instant lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
        this.token = token;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.lastLogin = lastLogin;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public Instant getRefreshTokenExpiry() { return refreshTokenExpiry; }
    public void setRefreshTokenExpiry(Instant refreshTokenExpiry) { this.refreshTokenExpiry = refreshTokenExpiry; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }
}
