package com._01Blog.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.UserMapper;
import com._01Blog.backend.model.dto.AuthResponse;
import com._01Blog.backend.model.dto.LoginDto;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto request) throws ExceptionProgram {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> register(@ModelAttribute RegisterDto request) throws ExceptionProgram {
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);

    }

    @GetMapping(path = "api/forme")
    public RegisterDto forMeDto(@RequestAttribute("user") User user) {
        return UserMapper.toDto(user);
    }

    // Get Profile
    @GetMapping(path = "api/profile/{id}")
    public RegisterDto profile(
            @RequestAttribute("user") User user,
            @PathVariable("id") UUID id) throws ExceptionProgram {
        return userService.profile(id, user);
    }

    @GetMapping(path = "api/search")
    public List<RegisterDto> searchUsers(@RequestParam("name") String name) {
        return userService.searchUsers(name);
    }

    // @PostMapping(value = "/register", consumes = "multipart/form-data")
    // public ResponseEntity<AuthResponse> register(
    // @RequestPart("user") RegisterDto request,
    // @RequestPart(value = "profileImage", required = false) MultipartFile
    // profileImage)
    // throws ExceptionProgram {

    // request.setProfileImage(profileImage);
    // AuthResponse response = userService.register(request);
    // return ResponseEntity.ok(response);
    // }

}
