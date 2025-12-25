package com._01Blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.service.CommentService;

import jakarta.validation.Valid;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.CommentDto;
import com._01Blog.backend.model.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // create comment
    @PostMapping(path = "create_comment")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto comment,
            @RequestAttribute("user") User user) throws ExceptionProgram {
        return commentService.createComment(comment, user);

    }

    // // delete comment
    // @PostMapping(path = "delete_comment")

    // // get comments
    // @GetMapping(path = "get_comments")

}
