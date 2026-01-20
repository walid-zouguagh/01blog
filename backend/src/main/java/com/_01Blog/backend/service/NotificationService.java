package com._01Blog.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.NotificationDto;
import com._01Blog.backend.model.entity.Notification;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.NotificationRepository;
import com._01Blog.backend.model.repository.SubscriptionRepository;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    // Set Notification
    @Async
    @Transactional
    public void setNotification(User fromUser, Post post) {
        int offset = 0;
        List<UUID> listIdUsers;
        UUID userId = fromUser.getId();
        do {
            listIdUsers = subscriptionRepository.getFollowers(userId, offset);
            if (listIdUsers == null || listIdUsers.size() == 0)
                break;

            for (UUID idUser : listIdUsers) {
                User toUser = userRepository.findById(idUser).orElse(null);
                if (toUser != null) {
                    saveNotification(fromUser, toUser, post, "created new post");
                }
            }
            offset += 10;
        } while (listIdUsers.size() == 10);
    }

    @Transactional
    public void saveNotification(User fromUser, User toUser, Post post, String message) {
        Notification notification = new Notification();
        notification.setToUser(toUser);
        notification.setFromUser(fromUser);
        notification.setRelatedPostId(post);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    // find all notification by user
    public List<NotificationDto> getAllNotificationsByUser(User user) {
        UUID userId = user.getId();
        List<NotificationDto> notifications = notificationRepository.getAllNotifications(userId);
        return notifications;
    }

    // count Notifications
    public int countNotification(User user) {
        UUID userId = user.getId();
        return notificationRepository.countNotification(userId);
    }

    public void readNotification(User user, UUID notifId) throws ExceptionProgram {
        UUID userId = user.getId();
        Notification notification = notificationRepository.findById(notifId)
                .orElseThrow(() -> new ExceptionProgram(400, "this notification not found"));
        if (notification != null && notification.getToUser().getId().equals(userId)) {
            notificationRepository.updateRead(notifId);
        } else {
            throw new ExceptionProgram(403, "you aren't authorized to read this notification");
        }
    }

    public void readAllNotification(User user) {
        UUID userId = user.getId();
        notificationRepository.updateAll(userId);
    }

}
