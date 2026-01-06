package com._01Blog.backend.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com._01Blog.backend.model.dto.CommentDto;
import com._01Blog.backend.model.dto.RegisterDto;
import com._01Blog.backend.model.entity.Comment;
import com._01Blog.backend.model.enums.Role;

@Component
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        CommentDto commentDto = new CommentDto(); 
        commentDto.setId(comment.getId());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUser(UserMapper.toDto(comment.getUser()));
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());

        return commentDto;
    }

    public static List<CommentDto> toDto(List<Map<String, Object>> commentList) {
        if (commentList == null) return null;

        return commentList.stream().map((comment) -> {
            
            CommentDto commentDto = new CommentDto(); 
            commentDto.setId((UUID) comment.get("id"));
            // commentDto.setPostId((UUID) comment.getPost().getId());
            commentDto.setContent((String) comment.get("content"));
            commentDto.setCreatedAt((LocalDateTime) comment.get("createdAt"));

            RegisterDto userDto = new RegisterDto();
            userDto.setId((UUID) comment.get("uid"));
            userDto.setUserName((String) comment.get("userName"));
            userDto.setFirstName((String) comment.get("firstName"));
            userDto.setLastName((String) comment.get("lastName"));
            userDto.setRole((Role) comment.get("role"));
            userDto.setUrlProfileImage((String) comment.get("profileImage"));
            commentDto.setUser(userDto);

            return commentDto;

        }).toList();
    }

}
