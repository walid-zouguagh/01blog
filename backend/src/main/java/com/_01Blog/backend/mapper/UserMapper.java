package com._01Blog.backend.mapper;

import java.util.Map;
import java.util.UUID;

import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;

public class UserMapper {
    public static RegisterDto toDto(User user) {
        if (user == null)
            return null;

        RegisterDto registerDto = new RegisterDto();
        registerDto.setId(user.getId());
        registerDto.setUserName(user.getUsername());
        registerDto.setFirstName(user.getFirstName());
        registerDto.setLastName(user.getLastName());
        registerDto.setUrlProfileImage(user.getProfileImage());
        registerDto.setEmail(user.getEmail());
        registerDto.setRole(user.getRole());
        registerDto.setBio(user.getBio());
        registerDto.setEnabled(user.getEnabled());
        return registerDto;
    }

    public static RegisterDto toDto(Map<String, Object> user) {
        if (user == null)
            return null;

        RegisterDto registerDto = new RegisterDto();
        registerDto.setId((UUID) user.get("id"));
        registerDto.setUserName((String) user.get("userName"));
        registerDto.setFirstName((String) user.get("firstName"));
        registerDto.setLastName((String) user.get("lastName"));
        registerDto.setUrlProfileImage((String) user.get("profileImage"));
        registerDto.setRole((Role) user.get("role"));
        registerDto.setBio((String) user.get("bio"));
        return registerDto;
    }

    public static User toUser(RegisterDto registerDto) {
        if (registerDto == null)
            return null;
        User user = new User();
        user.setUserName(registerDto.getUserName());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setBio(registerDto.getBio());
        user.setProfileImage(registerDto.getUrlProfileImage());
        return user;
    }

}
