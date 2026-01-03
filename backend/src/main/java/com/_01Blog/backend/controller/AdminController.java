package com._01Blog.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.model.dto.RegisterDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin")
public class AdminController {

    // Get Users for Admin
    @GetMapping(path = "/get-users")
    public List<RegisterDto> getUsers() {

    }


}
