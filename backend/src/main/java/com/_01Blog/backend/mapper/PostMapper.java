package com._01Blog.backend.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com._01Blog.backend.model.dto.MediaDto;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.entity.Post;

@Component
public class PostMapper {
    public PostDto toDto(Post post) {
        if (post == null)
            return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUser(post.getUser());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreateAt(post.getCreatedAt());

        if (post.getMedias() != null) {
            var mediaDtos = post.getMedias().stream()
                    .map(media -> new MediaDto(media.getUrl(), media.getType()))
                    .collect(Collectors.toList());
            dto.setMedia(mediaDtos);
        }
        return dto;

    }

}
