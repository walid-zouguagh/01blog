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
        dto.setId((UUID) post.get("id"));
        dto.setTitle((String) post.get("title"));
        dto.setContent((String) post.get("content"));

        // Handle Timestamp -> LocalDateTime
        Object createdAtObj = post.get("createdat");
        if (createdAtObj instanceof java.sql.Timestamp) {
            dto.setCreateAt(((java.sql.Timestamp) createdAtObj).toLocalDateTime());
        } else if (createdAtObj instanceof java.time.LocalDateTime) {
            dto.setCreateAt((java.time.LocalDateTime) createdAtObj);
        }

        dto.setNbrOfComments(((Number) post.get("totalcomments")).intValue());
        dto.setNbrOfLike(((Number) post.get("totallikes")).intValue());
        // Postgres lowercases aliases by default
        Object isLikedObj = post.get("isliked");
        dto.setLiked(isLikedObj != null && (Boolean) isLikedObj);

        RegisterDto userDto = new RegisterDto();
        userDto.setId((UUID) post.get("userid"));
        userDto.setUserName((String) post.get("username"));
        userDto.setFirstName((String) post.get("firstname"));
        userDto.setLastName((String) post.get("lastname"));
        userDto.setUrlProfileImage((String) post.get("profileimage"));

        // Handle Role string conversion
        Object roleObj = post.get("role");
        if (roleObj instanceof String) {
            try {
                userDto.setRole(Role.valueOf(((String) roleObj).toUpperCase()));
            } catch (Exception e) {
                userDto.setRole(Role.USER);
            }
        } else if (roleObj instanceof Role) {
            userDto.setRole((Role) roleObj);
        }

        dto.setUser(userDto);
        return dto;
    }

}
