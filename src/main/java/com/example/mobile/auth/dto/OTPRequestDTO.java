package com.example.mobile.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class OTPRequestDTO {
    @NotBlank(message = "OTP is required")
    private String otpCode;

    @NotBlank(message = "Identifier is required")
    private String identifier; // optional to verify which user

    // Getters & Setters
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}
