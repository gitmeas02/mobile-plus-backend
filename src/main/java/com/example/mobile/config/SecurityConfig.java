package com.example.mobile.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.mobile.auth.utils.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(auth -> auth
            // Public endpoints
            .requestMatchers(
                "/",                 // API root
                "/auth/**",          // OAuth2 auth endpoints
                "/otp/**",
                "/oauth2/**",
                "/error"
            ).permitAll()
            // Protected endpoints
            .requestMatchers("/users/**").authenticated()
            // All other endpoints require authentication
            .anyRequest().authenticated()
        );

        // Return JSON for unauthorized requests
        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
            })
        );

        // OAuth2 login
        http.oauth2Login(oauth2 -> oauth2
            .loginPage("/auth/oauth2") // custom endpoint
            .successHandler(oauth2LoginSuccessHandler)
            .failureHandler((request, response, exception) -> {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"OAuth2 authentication failed\",\"message\":\"" 
                    + exception.getMessage() + "\"}");
            })
        );

        // JWT resource server
        // http.oauth2ResourceServer(oauth2 -> 
        //     oauth2.jwt(jwt -> {})
        // );

        // Disable default login mechanisms
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        // Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = jwtSecret;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            return NimbusJwtDecoder.withSecretKey(secretKey).build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create JWT decoder", e);
        }
    }
}
