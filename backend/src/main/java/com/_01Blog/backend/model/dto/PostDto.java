package com._01Blog.backend.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.model.entity.User;
// import com._01Blog.backend.model.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
    private UUID id;
    private RegisterDto user;
    @NotBlank
    @Size(min = 3, max = 100)
    private String title;
    @NotBlank(message = "Content must be not null")
    @Size(min = 3, message = "content must be more than 3")
    private String content;
    private List<MediaDto> media;
    // private MediaType mediaType;
    // private String mediaUrl;
    private int nbrOfLike;
    private int nbrOfComments;
    private boolean isLiked;
    @JsonIgnore
    private MultipartFile[] images;
    private LocalDateTime createAt;

}
