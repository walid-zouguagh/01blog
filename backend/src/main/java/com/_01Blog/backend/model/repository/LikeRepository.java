package com._01Blog.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    @Query("""
            SELECT EXISTS(SELECT 1 FROM Like l
            WHERE l.postId = :postId AND l.userId = :userId)
            """)
    boolean isLiked(@Param("userId") UUID userId, @Param("postId") UUID postId);

    @Query("""
            DELETE FROM Like l WHERE l.userId = :userId AND l.postId = :postId
            """)
    void deleteLikeByPost(@Param("userId") UUID userId, @Param("postId") UUID postId);

    @Query("""
            SELECT DISTINCT COUNT(*) FROM Like l WHERE l.postId = :postId
            """)
    int countOfLikedPost(@Param("postId") UUID postId);
}
