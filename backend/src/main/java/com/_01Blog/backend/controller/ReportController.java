package com._01Blog.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.ReportDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // report Post
    @PostMapping(path = "/report-post")
    public void reportPost(
        @Valid @RequestBody ReportDto report,
        @RequestAttribute("user") User user

    ) throws ExceptionProgram{
        reportService.reportPost(report, user);
    }

    // report User
    @PostMapping(path = "/report-user")
    public void reportUser(
        @Valid @RequestBody ReportDto report,
        @RequestAttribute("user") User user

    ) throws ExceptionProgram{
        reportService.reportUser(report, user);
    }

}
