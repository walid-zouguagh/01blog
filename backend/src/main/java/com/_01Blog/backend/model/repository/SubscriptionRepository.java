package com._01Blog.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query("""
            SELECT EXISTS(SELECT 1 FROM Subscription WHERE follower_id = :userId AND following_id = :dataUser)
                """)
    boolean isFollowing(@Param("userId") UUID userId, @Param("dataUser") UUID id);
}
