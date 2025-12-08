package com.example.mobile.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobile.auth.dto.AuthResponse;
import com.example.mobile.auth.dto.ResendOTPRequest;
import com.example.mobile.auth.dto.VerifyOTPRequest;
import com.example.mobile.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/otp")
public class OTPController {
    
    @Autowired
    private AuthService authService;

    /**
     * Verify OTP code
     * POST /otp/verify
     */
    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyOTP(@Valid @RequestBody VerifyOTPRequest request) {
        try {
            AuthResponse response = authService.verifyOTP(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Resend OTP code
     * POST /otp/resend
     */
    @PostMapping("/resend")
    public ResponseEntity<AuthResponse> resendOTP(@Valid @RequestBody ResendOTPRequest request) {
        try {
            AuthResponse response = authService.resendOTP(request.getEmail(), request.getDeliveryMethod());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
