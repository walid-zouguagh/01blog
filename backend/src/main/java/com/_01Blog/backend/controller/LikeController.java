package com._01Blog.backend.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping(path = "like")
    public Map<String, Object> like(
        @RequestAttribute("user") User user,
        @RequestParam(defaultValue = "0", name = "postId") UUID postId
    ) throws ExceptionProgram {
        return likeService.like(user, postId);
    }

}
