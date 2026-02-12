package com._01Blog.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import com._01Blog.backend.model.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// JwtService (Generate + Validate Token)
@Service
public class JwtService {
    private static final long EXPIRATION = 1000L * 60 * 60 * 24;

    @Value("${jwt.key}")
    private String secretNuber;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretNuber.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            final String userId = extractUserId(token);

            boolean isUsernameValid = (username.equals(userDetails.getUsername()));
            boolean isUserIdValid = true;

            if (userDetails instanceof User) {
                isUserIdValid = userId.equals(((User) userDetails).getId().toString());
            }

            return (isUsernameValid && isUserIdValid && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

}