package com.example.mobile.auth.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

// import org.springframework.beans.factory.annotation.Value;

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
}
