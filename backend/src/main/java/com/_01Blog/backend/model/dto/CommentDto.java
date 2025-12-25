package com._01Blog.backend.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private UUID id;
    private UUID postId;
    private RegisterDto user;
    @Size(min = 3, max = 100, message = "content must be more than 3")
    private String content;
    private LocalDateTime createdAt;

}
