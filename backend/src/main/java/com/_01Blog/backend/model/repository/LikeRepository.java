package com._01Blog.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

        @Query(value = """
                        SELECT EXISTS(SELECT 1 FROM likes l
                        WHERE l.post_id = :postId AND l.user_id = :userId)
                        """, nativeQuery = true)
        boolean isLiked(@Param("userId") UUID userId, @Param("postId") UUID postId);

        @Query(value = """
                        DELETE FROM likes l WHERE l.user_id = :userId AND l.post_id = :postId
                        """, nativeQuery = true)
        void deleteLikeByPost(@Param("userId") UUID userId, @Param("postId") UUID postId);

        // SELECT COUNT(DISTINCT userId) FROM Like l WHERE l.postId = :postId;
        @Query(value = """
                        SELECT COUNT(*) FROM likes l WHERE l.post_id = :postId
                        """, nativeQuery = true)
        int countOfLikedPost(@Param("postId") UUID postId);
}
