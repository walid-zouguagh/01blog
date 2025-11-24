package com._01Blog.backend.model.dto;

import com._01Blog.backend.model.enums.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaDto {
    private String url;
    private MediaType type;
}
