package com._01Blog.backend.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "likes")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Like {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "post_id") // BIGINT FK → posts.id Target post
    private Post postId;

    @ManyToOne
    @JoinColumn(name = "user_id") // BIGINT FK → users.id Who liked
    private User userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
