package com._01Blog.backend.service;

import com._01Blog.backend.model.dto.AuthResponse;
import com._01Blog.backend.model.dto.LoginDto;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.repository.SubscriptionRepository;
import com._01Blog.backend.model.repository.UserRepository;
import com._01Blog.backend.util.Upload;

import jakarta.transaction.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SubscriptionRepository subscriptionRepository;

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

        var image = request.getProfileImage();
        if (image != null) {
            user.setProfileImage(Upload.saveImage(image));
        } else {
            user.setProfileImage("default-image.jpg");
        }

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

    public RegisterDto profile(UUID id, User user) throws ExceptionProgram {
        Map<String, Object> dataUser = userRepository.findUserData(id);
        if (dataUser == null || dataUser.size() == 0) {
            throw new ExceptionProgram(400, "User Not found");
        }
        RegisterDto userDto = UserMapper.toDto(dataUser);
        userDto.setMyAccount(userDto.getId().equals(user.getId()));
        if (!userDto.isMyAccount()) {
            boolean isFollowing = subscriptionRepository.isFollowing(user.getId(), (UUID) dataUser.get("id"));
            userDto.setHasConnect(isFollowing);
        }
        return userDto;
    }

    public List<RegisterDto> searchUsers(String name) {
        List<User> users = userRepository.searchUsers(name);
        return users
                .stream()
                .map((user) -> {
                    return UserMapper.toDto(user);
                }).toList();

    }
}
