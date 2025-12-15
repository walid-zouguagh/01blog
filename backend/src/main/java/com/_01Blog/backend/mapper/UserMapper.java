package com._01Blog.backend.mapper;

import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;

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
