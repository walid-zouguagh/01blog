package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findById(UUID id);

    @Query(value = """
            SELECT DISTINCT
            p.id, p.title, p.content, p.isHidden as IsHide, p.createdAt,
            u.id, u.userName, u.firstName, u.lastName, u.profileImage, u.role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM Like l2 WHERE l2.postId = p.id AND l2.userId = :userId) AS isLiked
            FROM Post p
            JOIN User u ON p.user = u.id
            LEFT JOIN Comment cm ON cm.postId = p.id
            LEFT JOIN Like l ON l.postId = p.id
            WHERE p.isHidden = false
            GROUP BY
                p.id, p.title, p.content, p.createdAt,
                u.id, u.userName, u.firstName, u.lastName, u.profileImage, u.role,
            ORDER BY p.createdAt DESC
            LIMIT 10 OFFSET :offset
            """)

    List<Map<String, Object>> getPosts(@Param("userId") UUID userId, @Param("offset") int offset);

    @Query(value = """
            SELECT DISTINCT
            p.id, p.title, p.content, p.isHidden AS IsHide, p.createdAt,
            u.id, u.userName, u.firstName, u.lastName, u.profileImage, u.role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM Like l2 WHERE l2.postId = p.id AND l2.userId = :userId) AS isLiked
            FROM Post p
            JOIN User u ON u.id = p.user
            LEFT JOIN Comment cm ON cm.postId = p.id
            LEFT JOIN Like l ON l.postId = p.id
            WHERE p.isHidden = false AND (EXISTS(SELECT 1 FROM Subscription WHERE followerId = :userId AND followingId = u.id) OR u.id = :userId)
            GROUP BY
                p.id, p.title, p.content, p.createdAt,
                u.id, u.userName, u.firstName, u.lastName, u.profileImage, u.role,
            ORDER BY p.createdAt DESC
            LIMIT 10 OFFSET :offset
            """)
    List<Map<String, Object>> getSubscribePosts(@Param("userId") UUID userId, @Param("offset") int offset);

}
