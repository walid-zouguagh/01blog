package com._01Blog.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
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

    // count Notifications
    @GetMapping(path = "countNotification")
    public int countNotification(@RequestAttribute("user") User user) {
       return notificationService.countNotification(user);
    }

    // read notification (user id and notification id)
    @PutMapping(path = "read-notification/{notifId}")
    public void readNotification(
        @RequestAttribute("user") User user, 
        @PathVariable UUID notifId
    ) throws ExceptionProgram{
        notificationService.readNotification(user, notifId);
    }

    // read all notification by user id 
    @PutMapping(path = "read-all-notification")
    public void readAllNotification(@RequestAttribute("user") User user) {
        notificationService.readAllNotification(user);
    }

}
