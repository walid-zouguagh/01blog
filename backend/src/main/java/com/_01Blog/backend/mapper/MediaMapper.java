package com._01Blog.backend.mapper;

import org.springframework.stereotype.Component;

import com._01Blog.backend.model.dto.MediaDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.PostMedia;

@Component
public class MediaMapper {
    public static PostMedia toEntity(MediaDto media, Post post) {
        PostMedia postMedia = new PostMedia();
        postMedia.setUrl(media.getUrl());
        postMedia.setType(media.getType());
        postMedia.setPost(post);
        return postMedia;
    }
}
