package com.example.mobile.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Send OTP code via email
     * @param toEmail recipient email address
     * @param otpCode the OTP code to send
     */
    public void sendOTPEmail(String toEmail, String otpCode, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your OTP Code - Mobile Plus Application");
            
            String emailBody = buildOTPEmailBody(username, otpCode);
            message.setText(emailBody);
            
            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again later.");
        }
    }

    /**
     * Build a professional OTP email body
     */
    private String buildOTPEmailBody(String username, String otpCode) {
        return String.format("""
            Hello %s,
            
            Your One-Time Password (OTP) for Mobile Plus is:
            
            %s
            
            This OTP is valid for 5 minutes. Please do not share this code with anyone.
            
            If you didn't request this code, please ignore this email or contact our support team.
            
            Best regards,
            Mobile Plus Team
            
            ---
            This is an automated message. Please do not reply to this email.
            """, 
            username, 
            otpCode
        );
    }

    /**
     * Send welcome email after successful registration
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Mobile Plus!");
            
            String emailBody = String.format("""
                Hello %s,
                
                Welcome to Mobile Plus! Your account has been successfully created.
                
                You can now log in and start using our services.
                
                If you have any questions, feel free to contact our support team.
                
                Best regards,
                Mobile Plus Team
                """, 
                username
            );
            
            message.setText(emailBody);
            mailSender.send(message);
        } catch (Exception e) {
            // Don't fail registration if welcome email fails
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
}
