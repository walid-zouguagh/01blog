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

// ❤️ Like Table
// Column	Type	Description
// id	BIGSERIAL PK	Like ID
// post_id	BIGINT FK → posts.id	Target post
// user_id	BIGINT FK → users.id	Who liked
// created_at	TIMESTAMP DEFAULT NOW()	Timestamp

@Entity
@Table(name = "like")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Like {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "post_id")  // BIGINT FK → posts.id	Target post
    private UUID postId;

    @Column(name = "user_id")  // BIGINT FK → users.id	Who liked
    private UUID userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    
}
