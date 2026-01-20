package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    // find report users

    @Query(value = """
            SELECT
                r.reported_user_id AS userId,
                u.id AS userId,
                u.user_name AS userName,
                u.first_name AS firstName,
                u.last_name AS lastName,
                u.profile_image AS profileImage,
                u.role AS role,
                u.enabled AS enabled,
                COUNT(r.id) AS countReport,
                MAX(r.created_at) AS lastReport
            FROM reports r
            JOIN users u ON u.id = r.reported_user_id
            WHERE r.reported_post_id IS NULL
            GROUP BY
                r.reported_user_id,
                u.id,u.user_name,u.first_name,u.last_name,u.profile_image, u.role, u.enabled
            ORDER BY r.created_at DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReportedUser();

    // find report post

    @Query(value = """
            SELECT
                r.reported_post_id AS postId,
                u.id AS userId,
                u.user_name AS userName,
                u.first_name AS firstName,
                u.last_name AS lastName,
                u.profile_image AS profileImage,
                SUBSTRING(p.title, 1, 20) AS title,
                SUBSTRING(p.content, 1, 100) AS content,
                p.is_hidden AS hidden,
                COUNT(r.id) AS countReported,
                MAX(r.created_at) AS lastReport
            FROM reports r
            JOIN posts p ON p.id = r.reported_post_id
            JOIN users u ON u.id = p.user_id
            WHERE r.reported_user_id IS NULL
            GROUP BY
                r.reported_post_id
                u.id,u.user_name,u.first_name,u.last_name,u.profile_image,
                p.title, p.content, p.is_hidden
            ORDER BY lastReport DESC
                """, nativeQuery = true)
    List<Map<String, Object>> getReportedPost();

    // find Reason User
    @Query(value = """
            SELECT
                r.id AS id,
                r.reason AS reason,
                r.created_at AS createdAt,
                u1.user_name AS username,
                u1.profile_image AS profileImage
            FROM reports r
            JOIN users u1 ON u1.id = r.reporter_id
            JOIN users u2 ON u2.id = r.reported_user_id
            WHERE u2.id = :userId
            GROUP BY r.id , r.reason , r.created_at , u1.user_name , u1.profile_image
            ORDER BY MAX(r.created_at) DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReasonReportedUser(@Param("userId") UUID userId);

    // find Reason Post
    @Query(value = """
            SELECT
                r.id AS id,
                r.reason AS reason,
                r.created_at AS createdAt,
                u.user_name AS username,
                u.profile_image AS profileImage
            FROM reports r
            JOIN users u ON u.id = r.reporter_id
            WHERE r.reported_post_id = :postId
            GROUP BY r.id , r.reason , r.created_at , u.user_name , u.profile_image
            ORDER BY MAX(r.created_at) DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReasonReportedPost(@Param("postId") UUID postId);

    @Query(value = """
            SELECT COUNT(*) FROM reports r WHERE r.reported_post_id IS NULL
            """, nativeQuery = true)
    int countReportUser();

    @Query(value = """
            SELECT COUNT(*) FROM reports r WHERE r.reported_user_id IS NULL
            """, nativeQuery = true)
    int countReportPost();
}
