package com._01Blog.backend.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.UserMapper;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.repository.PostRepository;
import com._01Blog.backend.model.repository.ReportRepository;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final ReportRepository reportRepository;

    // Get All Users for Admin
    public List<RegisterDto> getUsers(int offset, int limit) {
        List<RegisterDto> listUsers = userRepository.findAllUsers(offset, limit)
                .stream().map(UserMapper::toDto).toList();
        // .stream().map((user) -> UserMapper.toDto(user)).toList();
        return listUsers;
    }

    // Delete User by Admin
    @Transactional
    public void deleteUser(@NonNull UUID userId) throws ExceptionProgram {
        User user = userRepository.findById(userId).orElseThrow(() -> new ExceptionProgram(400, "not found this user"));
        if (user.getRole() == Role.ADMIN) {
            throw new ExceptionProgram(400, "you can't delete Admin");
        }
        // userRepository.deleteById(userId); // two methods correct
        userRepository.delete(user);
    }

    // Banne User by Admin
    @Transactional
    public boolean banneUser(@NonNull UUID userId) throws ExceptionProgram {
        User user = userRepository.findById(userId).orElseThrow(() -> new ExceptionProgram(400, "this user not found"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new ExceptionProgram(400, "you can't ban Admin"); // Fixed typo 'banned'
        }

        boolean newStatus = !user.getEnabled();
        userRepository.updateEnabledUser(userId, newStatus);

        return newStatus;
    }

    // hide post by admin
    @Transactional
    public boolean hidePost(@NonNull UUID postId) throws ExceptionProgram {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionProgram(400, "not found this post"));
        // postRepository.hidePost(postId, post.isHidden() ? false : true);
        postRepository.hidePost(postId, !post.isHidden());
        return !post.isHidden();
    }

    // Dashboard
    public Map<String, Object> dashboard() {
        return Map.of(
                "sumOfUsers", userRepository.count(),
                "reportedUsers", reportRepository.countReportUser(),
                "reportedPosts", reportRepository.countReportPost());
    }

    // Delete Reports (Dismiss)
    @Transactional
    public void deleteReports(UUID id, String type) {
        if ("USER".equalsIgnoreCase(type)) {
            reportRepository.deleteByReportedUserId(id);
            userRepository.updateEnabledUser(id, true); // Auto-Unban
        } else if ("POST".equalsIgnoreCase(type)) {
            reportRepository.deleteByReportedPostId(id);
            // Optional: Unhide post if we wanted similar logic?
            // For now, explicit User Unban only as requested.
        }
    }
}
