package com.example.mobile.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.example.mobile.roles.Role;
import com.example.mobile.roles.Scope;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties){
        this.jwtProperties= jwtProperties;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    
    public String generateAccessToken(String userId, String username, String email, Role role, String fullName, boolean enabled) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userId);
        userData.put("username", username);
        userData.put("fullName", fullName);
        userData.put("email", email);
        userData.put("role", role != null ? role.name() : Role.USER.name());
        userData.put("status", enabled ? "ACTIVE" : "INACTIVE");

        return Jwts.builder()
                .setIssuer("mobile-plus-backend")
                .setSubject(userId)
                .claim("user", userData)
                .claim("scope", Scope.READ_WRITE.getValue())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setIssuer("mobile-plus-backend")
                .setSubject(userId)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) claims.get("user");
        return user != null ? (String) user.get("username") : null;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) claims.get("user");
        String roleStr = user != null ? (String) user.get("role") : null;
        try {
            return roleStr != null ? Role.valueOf(roleStr) : Role.USER;
        } catch (IllegalArgumentException e) {
            return Role.USER; // Default
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) claims.get("user");
        return user != null ? (String) user.get("email") : null;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}