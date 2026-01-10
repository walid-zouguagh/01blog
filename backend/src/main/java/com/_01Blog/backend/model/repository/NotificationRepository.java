package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.model.dto.NotificationDto;
import com._01Blog.backend.model.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query(value = """
            SELECT
                n.id AS id,
                n.message AS message,
                n.createdAt AS createdAt,
                n.isRead AS isRead,
                COALESCE(n.relatedPostId, NULL) AS postId,
                u1.id AS fromUserId,
                u1.userName AS fromUserName,
                u1.profileImage AS photoUrl,
                u2.id AS toUserId,
                u2.userName AS toUserName,
            FROM Notification n 
            JOIN User u1 ON u1.id = n.fromUser
            JOIN User u2 ON u2.id = n.toUser
            WHERE n.toUser = :userId
            ORDER BY n.createdAt DESC
            """, nativeQuery = true)
    List<NotificationDto> getAllNotifications(@Param("userId") UUID userId);


    @Query(value = """
            SELECT COUNT(*) FROM Notification WHERE isRead = false AND toUser = :userId
            """, nativeQuery = true)
    int countNotification(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE Notification SET isRead = true WHERE id = :notifId
            """, nativeQuery = true)
    void updateRead(@Param("notifId") UUID notifId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE Notification SET isRead = true WHERE toUser = :userId
            """, nativeQuery = true)
    void updateAll(@Param("userId") UUID userId);
}
