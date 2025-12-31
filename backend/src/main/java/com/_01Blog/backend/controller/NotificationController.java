package com._01Blog.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.model.dto.NotificationDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class NotificationController {
    private final NotificationService notificationService;

    // find all notification by user
    @GetMapping(path = "notifications")
    public List<NotificationDto> getNotificationsByUser(
            @RequestAttribute("user") User user) {
        return notificationService.getAllNotificationsByUser(user);
    }

}
