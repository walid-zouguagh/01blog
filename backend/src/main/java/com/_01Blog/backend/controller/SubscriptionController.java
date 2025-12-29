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
import com._01Blog.backend.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("follow")
    public Map<String, Object> subscription(
            @RequestAttribute("user") User user,
            @RequestParam("userId") UUID userId) throws ExceptionProgram {

        return subscriptionService.subscription(user, userId);

    }

}
