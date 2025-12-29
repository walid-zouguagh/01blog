package com._01Blog.backend.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.SubscriptionRepository;
import com._01Blog.backend.model.repository.UserRepository;

@Service
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;

    Map<String, Object> subscription(User user, UUID userId) throws ExceptionProgram {
        User followingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionProgram(400, "user not found"));

        if (user.getId().equals(followingUser.getId())) {
            throw new ExceptionProgram(400, "sorry, you try follow your self");
        }

        

    }

}
