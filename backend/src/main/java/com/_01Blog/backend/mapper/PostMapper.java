package com._01Blog.backend.mapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com._01Blog.backend.model.dto.MediaDto;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.enums.Role;

@Component
public class PostMapper {

    public Post toPost(PostDto postDto) {
        if (postDto == null)
            return null;

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        if (post == null)
            return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUser(UserMapper.toDto(post.getUser()));
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

    public PostDto toDto(Map<String, Object> post) {
        if (post == null)
            return null;

        PostDto dto = new PostDto();
        dto.setId(((UUID) post.get("id")));
        // dto.setUser((User) post.get("user"));
        dto.setTitle((String) post.get("title"));
        dto.setContent((String) post.get("content"));
        dto.setCreateAt((LocalDateTime) post.get("createdAt"));
        dto.setNbrOfComments(((Number) post.get("totalComments")).intValue());
        dto.setNbrOfLike(((Number) post.get("totalLikes")).intValue());
        dto.setLiked((Boolean) post.get("isLiked"));

        RegisterDto userDto = new RegisterDto();
        userDto.setId((UUID) post.get("id"));
        userDto.setUserName((String) post.get("userName"));
        userDto.setFirstName((String) post.get("firstName"));
        userDto.setLastName((String) post.get("lastName"));
        userDto.setUrlProfileImage((String) post.get("profileImage"));
        userDto.setRole((Role) post.get("role"));
        dto.setUser(userDto);
        return dto;
    }

}
