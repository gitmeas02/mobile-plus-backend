package com.example.mobile.user.entity;

import com.example.mobile.projects.entity.ProjectEntities;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // this is user table
public class UserEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "Password should not be null")
    private String password;

    @ManyToMany(mappedBy = "teamMembers")
    @JsonIgnore
    private List<ProjectEntities> projects;

    private String phoneNumber;

    private String fullname;

    private String roles; // e.g., "USER", "ADMIN"

    private boolean enabled = true;

    private String otpCode;

    private Instant otpExpiry;

    // OAuth2 fields for social login
    @Column(name = "oauth_provider")
    private String oauthProvider; // "google", "github", "telegram", "local"

    @Column(name = "oauth_id")
    private String oauthId; // OAuth provider's user ID

    @Column(name = "telegram_chat_id")
    private String telegramChatId; // For Telegram OTP delivery

    @Column(name = "otp_delivery_method")
    private String otpDeliveryMethod; // "email" or "telegram"

    private boolean otpVerified = false;

    private String refreshToken;

    private Instant refreshTokenExpiry;

    private Instant lastLogin;

    private Instant createdAt;

    private Instant updatedAt;

    public UserEntity() {
    }

    // @PrePersist
    // public void prePersist() {
    //     if (this.id == null) {
    //         this.id = UUID.randomUUID().toString();
    //     }
    //     createdAt = Instant.now();
    //     updatedAt = Instant.now();
    // }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }



    // ===== Getters and Setters =====


     public List<ProjectEntities> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntities> projects) {
        this.projects = projects;
    }
     public String getFullName() {
        return this.fullname ; // Assuming username is the full name for simplicity
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public Instant getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(Instant otpExpiry) { this.otpExpiry = otpExpiry; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public Instant getRefreshTokenExpiry() { return refreshTokenExpiry; }
    public void setRefreshTokenExpiry(Instant refreshTokenExpiry) { this.refreshTokenExpiry = refreshTokenExpiry; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getOauthProvider() { return oauthProvider; }
    public void setOauthProvider(String oauthProvider) { this.oauthProvider = oauthProvider; }

    public String getOauthId() { return oauthId; }
    public void setOauthId(String oauthId) { this.oauthId = oauthId; }

    public String getTelegramChatId() { return telegramChatId; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }

    public String getOtpDeliveryMethod() { return otpDeliveryMethod; }
    public void setOtpDeliveryMethod(String otpDeliveryMethod) { this.otpDeliveryMethod = otpDeliveryMethod; }

    public boolean isOtpVerified() { return otpVerified; }
    public void setOtpVerified(boolean otpVerified) { this.otpVerified = otpVerified; }
}
