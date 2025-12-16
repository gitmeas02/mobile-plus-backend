package com.example.mobile.auth.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private final JwtProperties jwtProperties;
     //@Value("${jwt.secret}")
     // private String secret;

    public JwtUtil(JwtProperties jwtProperties){
        this.jwtProperties= jwtProperties;
    }
    @SuppressWarnings("deprecation")
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    @SuppressWarnings("deprecation")
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}