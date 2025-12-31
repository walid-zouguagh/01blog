package com._01Blog.backend.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class NotificationDto {

    private UUID id;

    private UUID fromUserId;
    private String fromUserName;
    private String photoUrl;

    private UUID toUserId;
    private String toUserName;

    private UUID postId;
    private String message;
    private boolean isRead;
    private LocalDateTime createAt;

}
