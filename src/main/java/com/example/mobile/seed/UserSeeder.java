package com.example.mobile.seed;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.repository.UserRepository;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Seeding database with initial users...");

        // Create admin user
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setEmail("admin@mobileplus.com");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setRoles("ADMIN,USER");
        admin.setEnabled(true);
        admin.setOtpVerified(true);
        admin.setOtpDeliveryMethod("EMAIL");
        admin.setCreatedAt(Instant.now());
        admin.setUpdatedAt(Instant.now());
        userRepository.save(admin);

        // Create test user with email OTP
        UserEntity user1 = new UserEntity();
        user1.setUsername("testuser");
        user1.setEmail("testuser@example.com");
        user1.setPassword(passwordEncoder.encode("Test@123"));
        user1.setRoles("USER");
        user1.setEnabled(true);
        user1.setOtpVerified(true);
        user1.setOtpDeliveryMethod("EMAIL");
        user1.setCreatedAt(Instant.now());
        user1.setUpdatedAt(Instant.now());
        userRepository.save(user1);

        // Create test user with Telegram OTP
        UserEntity user2 = new UserEntity();
        user2.setUsername("telegramuser");
        user2.setEmail("telegram@example.com");
        user2.setPassword(passwordEncoder.encode("Telegram@123"));
        user2.setRoles("USER");
        user2.setEnabled(true);
        user2.setOtpVerified(true);
        user2.setOtpDeliveryMethod("TELEGRAM");
        user2.setTelegramChatId("123456789"); // Example chat ID
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());
        userRepository.save(user2);

        // Create OAuth user (Google)
        UserEntity oauthUser = new UserEntity();
        oauthUser.setUsername("googleuser");
        oauthUser.setEmail("googleuser@gmail.com");
        oauthUser.setPassword(passwordEncoder.encode("Google@123")); // Fallback password
        oauthUser.setRoles("USER");
        oauthUser.setEnabled(true);
        oauthUser.setOtpVerified(true);
        oauthUser.setOauthProvider("google");
        oauthUser.setOauthId("google_123456");
        oauthUser.setCreatedAt(Instant.now());
        oauthUser.setUpdatedAt(Instant.now());
        userRepository.save(oauthUser);

        System.out.println("✅ Database seeded successfully!");
        System.out.println("-----------------------------------");
        System.out.println("Admin credentials:");
        System.out.println("  Email: admin@mobileplus.com");
        System.out.println("  Password: Admin@123");
        System.out.println("-----------------------------------");
        System.out.println("Test User 1 (Email OTP):");
        System.out.println("  Email: testuser@example.com");
        System.out.println("  Password: Test@123");
        System.out.println("-----------------------------------");
        System.out.println("Test User 2 (Telegram OTP):");
        System.out.println("  Email: telegram@example.com");
        System.out.println("  Password: Telegram@123");
        System.out.println("-----------------------------------");
        System.out.println("OAuth User (Google):");
        System.out.println("  Email: googleuser@gmail.com");
        System.out.println("-----------------------------------");
    }
}
