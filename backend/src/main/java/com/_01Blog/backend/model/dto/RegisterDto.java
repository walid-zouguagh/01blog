package com._01Blog.backend.model.dto;

import com._01Blog.backend.model.enums.Role;

import lombok.Data;

@Data
public class RegisterDto {

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String bio; // Description

    // private String profileImage;

    private Role role;
}
