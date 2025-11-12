package com._01Blog.backend.service;

import com._01Blog.backend.model.dto.AuthResponse;
import com._01Blog.backend.model.dto.LoginDto;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.repository.UserRepository;

import jakarta.transaction.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginDto request) throws ExceptionProgram {

        // ✅ 1. Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ExceptionProgram(400, "User not found"));

        // ✅ 2. Check password correctly
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ExceptionProgram(400, "Password incorrect");
        }

        // ✅ 3. Check if user is enabled
        if (!user.getEnabled()) {
            throw new ExceptionProgram(400, "You are banned");
        }

        // ✅ 4. Generate JWT
        String token = jwtService.generateToken(user);

        // ✅ 5. Build response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        // authResponse.setEmail(user.getEmail());
        // authResponse.setRole(user.getRole().name());

        return authResponse;
    }

    @Transactional
    public AuthResponse register(RegisterDto request) throws ExceptionProgram {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ExceptionProgram(400, "email already exists");
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ExceptionProgram(400, "username already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());

        // var image = request.getProfileImage();
        // if (image != null) {
        // user.setProfileImage(image);
        // } else {
        // user.setProfileImage("default-image.jpg");
        // }

        // Assign role
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }
}
