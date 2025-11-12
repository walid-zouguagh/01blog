package com._01Blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.AuthResponse;
import com._01Blog.backend.model.dto.LoginDto;
import com._01Blog.backend.model.dto.RegisterDto;
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
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDto request) throws ExceptionProgram {
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);

    }

}
