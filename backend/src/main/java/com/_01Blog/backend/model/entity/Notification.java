package com._01Blog.backend.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 🔔 Notification Table
// Column	Type	Description
// id	BIGSERIAL PK	Notification ID
// user_id	BIGINT FK → users.id	Recipient
// related_user_id	BIGINT FK → users.id	Related
// related_post_id	BIGINT NULL FK → posts.id	Optional link to a post
// message	TEXT	Content (e.g., “John posted a new article”)
// is_read	BOOLEAN DEFAULT FALSE	Status
// created_at	TIMESTAMP DEFAULT NOW()	Timestamp


@Table(name = "notification")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notification {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "related_user_id")
    private UUID relatedUserId;

    @Column(name = "related_post_id")
    private UUID relatedPostId;

    @Column(name = "message") // Content (e.g., “John posted a new article”)
    private String message;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
