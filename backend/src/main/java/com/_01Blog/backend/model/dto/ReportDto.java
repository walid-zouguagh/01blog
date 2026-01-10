package com._01Blog.backend.model.dto;

import java.util.UUID;

// import com._01Blog.backend.model.enums.StatusReport;
// import com._01Blog.backend.model.enums.TypeReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportDto {
    private UUID id;

    private UUID reportedUserId;
    private UUID reportedPostId;

    // private RegisterDto reporter;
    // private RegisterDto reportedUser;
    // private PostDto reportedPost;
    // private TypeReport typeReport; // User , Post
    @NotBlank(message = "you must create a message")
    @Size(min = 10, message = "The message must be longer than 10 characters")
    private String reason;
    // private StatusReport statusReport; // PENDING, RESOLVED, DISMISSED
}
