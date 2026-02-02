package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Override
    @NonNull
    Optional<Post> findById(@NonNull UUID id);

    @Query(value = """
            SELECT DISTINCT
            p.id AS id, p.title AS title, p.content AS content, p.is_hidden as IsHide, p.created_at AS createdAt,
            u.id AS userId, u.user_name AS userName, u.first_name AS firstName, u.last_name AS lastName, u.profile_image AS profileImage, u.role AS role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM likes l2 WHERE l2.post_id = p.id AND l2.user_id = :userId) AS isLiked
            FROM posts p
            JOIN users u ON p.user_id = u.id
            LEFT JOIN comments cm ON cm.post_id = p.id
            LEFT JOIN likes l ON l.post_id = p.id
            WHERE p.is_hidden = false
            GROUP BY
                p.id, p.title, p.content, p.created_at,
                u.id, u.user_name, u.first_name, u.last_name, u.profile_image, u.role
            ORDER BY p.created_at DESC
            LIMIT 10 OFFSET :offset
            """, nativeQuery = true)

    List<Map<String, Object>> getPosts(@Param("userId") UUID userId, @Param("offset") int offset);

    @Query(value = """
            SELECT DISTINCT
            p.id AS id, p.title AS title, p.content AS content, p.is_hidden AS IsHide, p.created_at AS createdAt,
            u.id AS userId, u.user_name AS userName, u.first_name AS firstName, u.last_name AS lastName, u.profile_image AS profileImage, u.role AS role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM likes l2 WHERE l2.post_id = p.id AND l2.user_id = :userId) AS isLiked
            FROM posts p
            JOIN users u ON u.id = p.user_id
            LEFT JOIN comments cm ON cm.post_id = p.id
            LEFT JOIN likes l ON l.post_id = p.id
            WHERE p.is_hidden = false AND (EXISTS(SELECT 1 FROM subscriptions WHERE follower_id = :userId AND following_id = u.id) OR u.id = :userId)
            GROUP BY
                p.id, p.title, p.content, p.created_at,
                u.id, u.user_name, u.first_name, u.last_name, u.profile_image, u.role
            ORDER BY p.created_at DESC
            LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<Map<String, Object>> getSubscribePosts(@Param("userId") UUID userId, @Param("offset") int offset);

    @Query(value = """
            SELECT DISTINCT
            p.id AS id, p.title AS title, p.content AS content, p.is_hidden AS IsHide, p.created_at AS createdAt,
            u.id AS userId, u.user_name AS userName, u.first_name AS firstName, u.last_name AS lastName, u.profile_image AS profileImage, u.role AS role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM likes l2 WHERE l2.post_id = p.id AND l2.user_id = :userId) AS isLiked
            FROM posts p
            JOIN users u ON u.id = p.user_id
            LEFT JOIN comments cm ON cm.post_id = p.id
            LEFT JOIN likes l ON l.post_id = p.id
            WHERE p.is_hidden = false AND u.id = :idUserProfile
            GROUP BY
                p.id, p.title, p.content, p.created_at,
                u.id, u.user_name, u.first_name, u.last_name, u.profile_image, u.role
            ORDER BY p.created_at DESC
            LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<Map<String, Object>> getPostsUser(@Param("userId") UUID userId, @Param("offset") int offset,
            @Param("idUserProfile") UUID idUserProfile);

    @Query(value = """
            SELECT DISTINCT
            p.id AS id, p.title AS title, p.content AS content, p.is_hidden AS IsHide, p.created_at AS createdAt,
            u.id AS userId, u.user_name AS userName, u.first_name AS firstName, u.last_name AS lastName, u.profile_image AS profileImage, u.role AS role,
            COUNT(DISTINCT cm.id) AS totalComments,
            COUNT(DISTINCT l.id) AS totalLikes,
            EXISTS(SELECT 1 FROM likes l2 WHERE l2.post_id = p.id AND l2.user_id = :userId) AS isLiked
            FROM posts p
            JOIN users u ON u.id = p.user_id
            LEFT JOIN comments cm ON cm.post_id = p.id
            LEFT JOIN likes l ON l.post_id = p.id
            WHERE p.is_hidden = false AND p.id = :postId
            GROUP BY
                p.id, p.title, p.content, p.created_at,
                u.id, u.user_name, u.first_name, u.last_name, u.profile_image, u.role
            ORDER BY p.created_at DESC
            """, nativeQuery = true)
    Map<String, Object> getPost(@Param("userId") UUID userId, @Param("postId") UUID postId);

    @Query(value = """
            UPDATE posts SET is_hidden = :isHiden WHERE id = :postId
            """, nativeQuery = true)
    void hidePost(@Param("postId") UUID postId, @Param("isHiden") boolean isHidden);

}
