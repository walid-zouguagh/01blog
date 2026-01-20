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
                            n.created_at AS createdAt,
                            n.is_read AS isRead,
                            COALESCE(n.related_post_id, NULL) AS postId,
                            u1.id AS fromUserId,
                            u1.user_name AS fromUserName,
                            u1.profile_image AS photoUrl,
                            u2.id AS toUserId,
                            u2.user_name AS toUserName,
                        FROM notifications n
                        JOIN users u1 ON u1.id = n.user_id
                        JOIN users u2 ON u2.id = n.related_user_id
                        WHERE n.related_user_id = :userId
                        ORDER BY n.created_at DESC
                        """, nativeQuery = true)
        List<NotificationDto> getAllNotifications(@Param("userId") UUID userId);

        @Query(value = """
                        SELECT COUNT(*) FROM notification WHERE is_read = false AND related_user_id = :userId
                        """, nativeQuery = true)
        int countNotification(@Param("userId") UUID userId);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE notification SET is_read = true WHERE id = :notifId
                        """, nativeQuery = true)
        void updateRead(@Param("notifId") UUID notifId);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE notification SET is_read = true WHERE related_user_id = :userId
                        """, nativeQuery = true)
        void updateAll(@Param("userId") UUID userId);
}
