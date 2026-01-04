package com._01Blog.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin")
public class AdminController {
    public final AdminService adminService;

    // Get All Users for Admin
    @GetMapping(path = "/get-users")
    public List<RegisterDto> getUsers(
        @RequestParam(name = "offset", defaultValue = "0") int offset,
        @RequestParam(name = "limit", defaultValue = "0") int limit
    ) {
        return adminService.getUsers(offset, limit);
    }

    // Delete User
    @DeleteMapping(path = "/delete-user")
    public ResponseEntity<?> deleteUser(
        @RequestParam("userId") UUID userId
    ) throws ExceptionProgram {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("userId", userId));
    }

    // Banne User by Admin

}
