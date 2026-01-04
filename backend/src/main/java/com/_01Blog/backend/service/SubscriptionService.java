package com._01Blog.backend.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.Subscription;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.SubscriptionRepository;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    // private NotificationService notificationService;

    @Transactional
    public Map<String, Object> subscription(User user, @NonNull UUID userId) throws ExceptionProgram {
        User followingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionProgram(400, "user not found"));

        if (user.getId().equals(followingUser.getId())) {
            throw new ExceptionProgram(400, "sorry, you try follow your self");
        }

        boolean isFollowing = subscriptionRepository.isFollowing(user.getId(), followingUser.getId());

        if (isFollowing) {
            // delete subscription && delete notification
            subscriptionRepository.deleteSubscription(user.getId(), followingUser.getId());
            // notificationService.deleteNotification(followingUser.getId(), user.getId());
        } else {
            // add subscription
            // add notification

            Subscription subscription = new Subscription();
            subscription.setFollowerId(user);
            subscription.setFollowingId(followingUser);
            subscriptionRepository.save(subscription);
        }
        return Map.of(
                "isFollowing", !isFollowing,
                "follower", subscriptionRepository.countOfFollower(followingUser.getId()),
                "following", subscriptionRepository.countOfFollowing(followingUser.getId()));

    }

}
