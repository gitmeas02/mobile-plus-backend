package com.example.mobile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                .requestMatchers("/users/create", "/users/all").permitAll() // Add public user endpoints
                // .requestMatchers("/api/public/**").permitAll() // Example: allow all /api/public/* endpoints
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
        );

        // Use new syntax instead of deprecated httpBasic()
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Needed for encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Needed for AuthenticationManager injection
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
