package com._01Blog.backend.model.dto;

import com._01Blog.backend.model.enums.MediaType;

import lombok.Data;

@Data
public class MediaDto {
    private String url;
    private MediaType type;
}
