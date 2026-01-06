package com._01Blog.backend.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

// import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
@io.micrometer.common.lang.NonNullFields
public class CommentDto {
    private UUID id;
    @NotNull(message = "post id is required")
    @NonNull
    private UUID postId;
    private RegisterDto user;
    @Size(min = 3, max = 100, message = "content must be more than 3")
    private String content;
    private LocalDateTime createdAt;

    public CommentDto() {}
    public CommentDto(@NonNull UUID postId) {
        this.postId = postId;
    }

}
