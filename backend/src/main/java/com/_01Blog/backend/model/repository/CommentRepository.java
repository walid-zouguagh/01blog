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
public interface CommentRepository extends JpaRepository<Comment, UUID>{
    @Query(value = """
            SELECT
                cm.id,
                cm.content,
                cm.createdAt AS createdAt,
                p.id AS postId,

                u.id AS uid,
                u.userName AS userName,
                u.firstName AS firstName,
                u.lastName AS lastName,
                u.role AS role,
                u.profileImage AS profileImage

                FROM Comment cm
                JOIN User u ON u.id = cm.user
                JOIN Post p ON p.id = cm.post
                WHERE p.isHidden = false AND p.id = :postId
                GROUP BY 
                    cm.id, cm.content, cm.createdAt,
                    u.id, u.userName, u.firstName, u.lastName, u.role
                ORDER BY cm.createdAt DESC
                LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<Map<String, Object>> getComments(@Param("postId") UUID postId, @Param("userId") UUID userId, @Param("offset") int offset);

}
