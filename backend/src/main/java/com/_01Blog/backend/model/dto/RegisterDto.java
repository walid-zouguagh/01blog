package com._01Blog.backend.model.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.enums.StatusReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    private UUID id;

    // @UserName interface
    @NotBlank(message = "First name is required.")
    @Size(min = 3, max = 25, message = "The first name must be between 3 and 25 characters long.")
    private String userName;

    // @ValidName interface
    @NotBlank(message = "First name is required.")
    @Size(min = 3, max = 25, message = "The first name must be between 3 and 25 characters long.")
    private String firstName;

    // @ValidName interface
    @NotBlank(message = "Last name is required.")
    @Size(min = 3, max = 25, message = "The last name must be between 3 and 25 characters long.")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is required")
    @Size(min = 3, message = "password must be more than 3 characters")
    private String password;

    private String bio; // Description

    @JsonIgnore
    private MultipartFile profileImage;
    private String urlProfileImage;
    private int followers;
    private int following;
    private Role role;
    private Boolean enabled;
}
