package com._01Blog.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.UserMapper;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    public final UserRepository userRepository;

    // Get All Users for Admin
    public List<RegisterDto> getUsers(int offset, int limit) {
        List<RegisterDto> listUsers = userRepository.findAllUsers(offset, limit)
                                    .stream().map(UserMapper::toDto).toList();
                                    // .stream().map((user) -> UserMapper.toDto(user)).toList();
        return listUsers;
    }

    // Delete User by Admin
    @Transactional
    public void deleteUser(@NonNull UUID userId) throws ExceptionProgram{
        User user = userRepository.findById(userId).orElseThrow(()-> new ExceptionProgram(400, "not found this user"));
        if (user.getRole() == Role.ADMIN) {
            throw new ExceptionProgram(400, "you can't delete Admin");
        }
        // userRepository.deleteById(userId); // two methods correct
        userRepository.delete(user);
    }
}
