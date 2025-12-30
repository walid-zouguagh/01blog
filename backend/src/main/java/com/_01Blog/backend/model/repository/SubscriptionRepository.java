package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

        @Query("""
                        SELECT EXISTS(SELECT 1 FROM Subscription WHERE followerId = :userId AND followingId = :dataUser)
                            """)
        boolean isFollowing(@Param("userId") UUID userId, @Param("dataUser") UUID id);

        @Query("""
                        DELETE FROM Subscription s WHERE s.followerId = :userId AND s.followingId = :followingUserId
                        """)
        void deleteSubscription(@Param("userId") UUID userId, @Param("followingUserId") UUID followingUserId);

        @Query("""
                        SELECT COUNT(DISTINCT s.followerId) FROM Subscription s WHERE s.followingId = :userId
                        """)
        int countOfFollower(@Param("userId") UUID userId);

        @Query("""
                        SELECT COUNT(DISTINCT s.followingId) FROM Subscription s WHERE s.followerId = :userId
                        """)
        int countOfFollowing(@Param("userId") UUID userId);

        @Query("""
                        SELECT s.followerId FROM Subscription s WHERE s.followingId = :userId LIMIT 10 OFFSET :offset
                        """)
        List<UUID> getFollowers(@Param("userId") UUID userId, @Param("offset") int offset);

        @Query("""
                        SELECT s.followingId FROM Subscription s WHERE s.followerId = :userId LIMIT 10 OFFSET :offset
                        """)
        List<UUID> getFollowing(@Param("userId") UUID userId, @Param("offset") int offset);

}
