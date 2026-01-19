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

        @Query(value = """
                        SELECT EXISTS(SELECT 1 FROM subscription WHERE follower_id = :userId AND following_id = :dataUser)
                            """, nativeQuery = true)
        boolean isFollowing(@Param("userId") UUID userId, @Param("dataUser") UUID id);

        @Query(value = """
                        DELETE FROM subscription s WHERE s.follower_id = :userId AND s.following_id = :followingUserId
                        """, nativeQuery = true)
        void deleteSubscription(@Param("userId") UUID userId, @Param("followingUserId") UUID followingUserId);

        @Query(value = """
                        SELECT COUNT(DISTINCT s.follower_id) FROM subscription s WHERE s.following_id = :userId
                        """, nativeQuery = true)
        int countOfFollower(@Param("userId") UUID userId);

        @Query(value = """
                        SELECT COUNT(DISTINCT s.following_id) FROM subscription s WHERE s.follower_id = :userId
                        """, nativeQuery = true)
        int countOfFollowing(@Param("userId") UUID userId);

        @Query(value = """
                        SELECT s.follower_id FROM subscription s WHERE s.following_id = :userId LIMIT 10 OFFSET :offset
                        """, nativeQuery = true)
        List<UUID> getFollowers(@Param("userId") UUID userId, @Param("offset") int offset);

        @Query(value = """
                        SELECT s.following_id FROM subscription s WHERE s.follower_id = :userId LIMIT 10 OFFSET :offset
                        """, nativeQuery = true)
        List<UUID> getFollowing(@Param("userId") UUID userId, @Param("offset") int offset);

}
