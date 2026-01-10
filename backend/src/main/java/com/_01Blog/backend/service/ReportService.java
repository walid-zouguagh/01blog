package com._01Blog.backend.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.ReportDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.Report;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.enums.StatusReport;
import com._01Blog.backend.model.enums.TypeReport;
import com._01Blog.backend.model.repository.PostRepository;
import com._01Blog.backend.model.repository.ReportRepository;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // Reported the post
    @Transactional
    public void reportPost(ReportDto reportDto, User user) throws ExceptionProgram {
        if (reportDto.getReportedPostId() == null) {
            return;
        }

        // Get Post
        Post post = postRepository.findById(reportDto.getReportedPostId())
                .orElseThrow(() -> new ExceptionProgram(400, "this post not found"));

        // check Admin
        if (user.getRole().equals(Role.ADMIN)) {
            throw new ExceptionProgram(400, "Admin can't Report");
        }

        // check user report his post
        if (user.getId().equals(post.getUser().getId())) {
            throw new ExceptionProgram(400, "you can't report your post");
        }

        // check if post is hidden
        if (post.isHidden()) {
            throw new ExceptionProgram(400, "you can't report a hidden post");
        }

        Report report = new Report();
        report.setReporterId(user);
        report.setReportedPostId(post);
        report.setReason(reportDto.getReason());
        report.setType(TypeReport.POST);
        report.setStatus(StatusReport.PENDING);

        reportRepository.save(report);

    }

    @Transactional
    public void reportUser(ReportDto reportDto, User user) throws ExceptionProgram {
        // check user reported
        User userReported = userRepository.findById(reportDto.getReportedUserId())
                .orElseThrow(() -> new ExceptionProgram(400, "this user not found"));

        // check user is admin
        if (user.getRole().equals(Role.ADMIN)) {
            throw new ExceptionProgram(400, "Admin can't report");
        }

        // check user report yourself
        if (userReported.getId().equals(user.getId())) {
            throw new ExceptionProgram(400, "you can't report yourself");
        }

        Report report = new Report();
        report.setReporterId(user);
        report.setReportedUserId(userReported);
        report.setType(TypeReport.USER);
        report.setReason(reportDto.getReason());
        report.setStatus(StatusReport.PENDING);
        reportRepository.save(report);

    }

    public List<Map<String, Object>> getReportedUser() {
        return reportRepository.getReportedUser();
    }

    public List<Map<String, Object>> getReportedPost() {
        return reportRepository.getReportedPost();
    }

    public List<Map<String, Object>> getReasonReportedUser(UUID userId) {
        return reportRepository.getReasonReportedUser(userId);
    }

    public List<Map<String, Object>> getReasonReportedPost(UUID postId) {
        return reportRepository.getReasonReportedPost(postId);
    }

}
