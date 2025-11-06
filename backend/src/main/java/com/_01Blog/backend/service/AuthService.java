package com._01Blog.backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.AuthResponse;
import com._01Blog.backend.model.dto.LoginDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginDto request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ExceptionProgram(400, "user not found"));

        if (passwordEncoder.matches(user.getPassword(), request.getPassword())) {
            throw new ExceptionProgram(400, "password incorrect");
        }

        return user;
    }
}
