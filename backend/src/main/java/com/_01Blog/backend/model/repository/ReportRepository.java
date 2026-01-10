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
                r.reportedUserId AS userId,
                u.id AS userId,
                u.userName AS userName,
                u.firstName AS firstName,
                u.lastName AS lastName,
                u.profileImage AS profileImage,
                u.role AS role,
                u.enabled AS enabled,
                COUNT(r.id) AS countReport,
                MAX(r.createdAt) AS lastReport
            FROM Report r
            JOIN User u ON u.id = r.reportedUserId
            WHERE r.reportedPostId IS NULL
            GROUP BY
                r.reportedUserId,
                u.id,u.userName,u.firstName,u.lastName,u.profileImage, u.role, u.enabled
            ORDER BY r.createdAt DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReportedUser();

    // find report post

    @Query(value = """
            SELECT
                r.reportedPostId AS postId,
                u.id AS userId,
                u.userName AS userName,
                u.firstName AS firstName,
                u.lastName AS lastName,
                u.profileImage AS profileImage,
                SUBSTRING(p.title, 1, 20) AS title,
                SUBSTRING(p.content, 1, 100) AS content,
                p.isHidden AS hidden,
                COUNT(r.id) AS countReported,
                MAX(r.createdAt) AS lastReport
            FROM Report r
            JOIN Post p ON p.id = r.reportedPostId
            JOIN User u ON u.id = p.user
            WHERE r.reportedUserId IS NULL
            GROUP BY
                r.reportedPostId
                u.id,u.userName,u.firstName,u.lastName,u.profileImage,
                p.title, p.content, p.isHidden
            ORDER BY lastReport DESC
                """, nativeQuery = true)
    List<Map<String, Object>> getReportedPost();

    // find Reason User
    @Query(value = """
            SELECT
                r.id AS id,
                r.reason AS reason,
                r.createdAt AS createdAt,
                u1.userName AS username,
                u1.profileImage AS profileImage
            FROM Report r
            JOIN User u1 ON u1.id = r.reporterId
            JOIN User u2 ON u2.id = r.reportedUserId
            WHERE u2.id = :userId
            GROUP BY r.id , r.reason , r.createdAt , u1.userName , u1.profileImage
            ORDER BY MAX(r.createdAt) DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReasonReportedUser(@Param("userId") UUID userId);

    // find Reason Post
    @Query(value = """
            SELECT
                r.id AS id,
                r.reason AS reason,
                r.createdAt AS createdAt,
                u1.userName AS username,
                u1.profileImage AS profileImage
            FROM Report r
            JOIN User u ON u.id = r.reporterId
            WHERE r.reportedPostId = :postId
            GROUP BY r.id , r.reason , r.createdAt , u1.userName , u1.profileImage
            ORDER BY MAX(r.createdAt) DESC
            """, nativeQuery = true)
    List<Map<String, Object>> getReasonReportedPost(@Param("postId") UUID postId);

    @Query(value = """
            SELECT COUNT(*) FROM Report r WHERE r.reportedPostId IS NULL
            """, nativeQuery = true)
    int countReportUser();

    @Query(value = """
            SELECT COUNT(*) FROM Report r WHERE r.reportedUserId IS NULL
            """, nativeQuery = true)
    int countReportPost();
}
