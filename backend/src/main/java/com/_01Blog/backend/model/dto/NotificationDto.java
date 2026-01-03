package com._01Blog.backend.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationDto {

    UUID getId();

    UUID getFromUserId();
    String getFromUserName();
    String getPhotoUrl();

    UUID getToUserId();
    String getToUserName();

    UUID getPostId();
    String getMessage();
    boolean getIsRead();
    LocalDateTime getCreateAt();

}
