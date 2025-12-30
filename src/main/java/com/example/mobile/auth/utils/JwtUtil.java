package com.example.mobile.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import com.example.mobile.roles.Role;
import com.example.mobile.roles.Scope;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties){
        this.jwtProperties= jwtProperties;
    }
    
    public String generateAccessToken(String userId, String username, String email, Role role) {
        return Jwts.builder()
                .setIssuer("mobile-plus-backend")
                .setSubject(userId)
                .claim("username", username)
                .claim("email", email)
                .claim("role", role != null ? role.name() : Role.USER.name())
                .claim("scope", Scope.READ_WRITE.getValue())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setIssuer("mobile-plus-backend")
                .setSubject(userId)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.get("username", String.class);
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        String roleStr = claims.get("role", String.class);
        try {
            return Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            return Role.USER; // Default
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.get("email", String.class);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}