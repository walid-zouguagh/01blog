package com._01Blog.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// import org.springframework.boot.autoconfigure.ssl.SslBundleProperties.Key;
import org.springframework.stereotype.Service;

import com._01Blog.backend.model.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// JwtService (Generate + Validate Token)
@Service
public class JwtService {
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;
    private static final String SECRET_NUMBER = "3f1a5d7e464d59d087c80fad04bf45161bc0258b46308d9fad2d922ae9fb9339d9028caaf42ce32f";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_NUMBER.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
