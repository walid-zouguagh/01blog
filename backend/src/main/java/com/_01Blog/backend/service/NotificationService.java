package com._01Blog.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com._01Blog.backend.model.dto.NotificationDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.NotificationRepository;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;

    public List<NotificationDto> getAllNotificationsByUser(User user) {
        UUID userId = user.getId();
        List<NotificationDto> notifications = notificationRepository.getAllNotifications(userId);
        return notifications;
    }

}
