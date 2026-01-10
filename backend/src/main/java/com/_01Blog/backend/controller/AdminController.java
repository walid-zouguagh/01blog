package com._01Blog.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.enums.TypeReport;
import com._01Blog.backend.service.AdminService;
import com._01Blog.backend.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {
    public final AdminService adminService;
    public final ReportService reportService;

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
    @PutMapping(path = "/banne-user")
    public boolean banneUser(
        @RequestParam("userId") UUID userId
    ) throws ExceptionProgram {
        return adminService.banneUser(userId);
    }

    // Hide Post By Admin
    @PutMapping(path = "/hide-post")
    public boolean hidePost(
        @RequestParam("postId") UUID postId
    ) throws ExceptionProgram{
        return adminService.hidePost(postId);
    }

    // get Reported : Report User | Post
    @GetMapping(path = "/reported")
    public List<Map<String, Object>> getReported(@RequestParam(name = "type") TypeReport type) throws ExceptionProgram {
        if (type.equals(TypeReport.USER)) {
            return reportService.getReportedUser();
        }else if (type.equals(TypeReport.POST)) {
            return reportService.getReportedPost();
        }
        return null;
    }

    // get Reason Reported
    @GetMapping(path = "/reason/user")
    public List<Map<String, Object>> getReasonReportedUser(
        @RequestParam(name = "userId") UUID userId
    ) throws ExceptionProgram{
        return reportService.getReasonReportedUser(userId);
    }

    // get Reason Post
    @GetMapping(path = "/reason/post")
    public List<Map<String, Object>> getReasonReportedPost(
        @RequestParam(name = "postId") UUID postId
    ) throws ExceptionProgram{
        return reportService.getReasonReportedPost(postId);
    }

    // Dashboard
    @GetMapping(path = "/dashboard")
    public Map<String, Object> dashboard() {
        return adminService.dashboard();
    }

}
