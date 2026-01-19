package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query(value = """
            SELECT
                cm.id,
                cm.content,
                cm.created_at AS createdAt,
                p.id AS postId,

                u.id AS uid,
                u.user_name AS userName,
                u.first_name AS firstName,
                u.last_name AS lastName,
                u.role AS role,
                u.profile_image AS profileImage

                FROM comment cm
                JOIN user u ON u.id = cm.user_id
                JOIN post p ON p.id = cm.post_id
                WHERE p.is_hidden = false AND p.id = :postId
                GROUP BY
                    cm.id, cm.content, cm.created_at,
                    u.id, u.user_name, u.first_name, u.last_name, u.role
                ORDER BY cm.created_at DESC
                LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<Map<String, Object>> getComments(@Param("postId") UUID postId, @Param("userId") UUID userId,
            @Param("offset") int offset);

}
