package com._01Blog.backend.service;

import java.security.Key;

// import org.springframework.boot.autoconfigure.ssl.SslBundleProperties.Key;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;
    private static final String SECRET_NUMBER = "3f1a5d7e464d59d087c80fad04bf45161bc0258b46308d9fad2d922ae9fb9339d9028caaf42ce32f";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_NUMBER.getBytes());
    }

}
