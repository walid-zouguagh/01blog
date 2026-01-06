package com._01Blog.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.service.CommentService;

import jakarta.validation.Valid;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.CommentDto;
import com._01Blog.backend.model.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class CommentController {
    private final CommentService commentService;

    // get comments
    @GetMapping(path = "get_comments")
    public List<CommentDto> getComments(
        @RequestAttribute("userId") UUID userId,
        @RequestParam(defaultValue = "0", name = "postId") UUID postId,
        @RequestParam(defaultValue = "0", name = "offset") int offset) {
            return commentService.getComments(postId, userId, offset);
    }


    // create comment
    @PostMapping(path = "create_comment")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentDto commentDto,
            @RequestAttribute("user") User user) throws ExceptionProgram {
        return ResponseEntity.ok(commentService.createComment(commentDto, user));
    }

    // delete comment
    @DeleteMapping("/comments/{commentId}")  // ← Use DELETE, not POST!
    public ResponseEntity<?> deleteComment(
        @NonNull @PathVariable UUID commentId,
        @RequestAttribute("user") User currentUser) throws ExceptionProgram {

        commentService.deleteComment(currentUser, commentId);
    
        return ResponseEntity.ok().body(Map.of("message", "Comment deleted successfully"));
    }
    



}
