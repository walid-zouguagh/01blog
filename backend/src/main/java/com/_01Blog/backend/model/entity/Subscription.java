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

// 👥 Subscription Table
// Column	Type	Description
// id	BIGSERIAL PK	Subscription ID
// follower_id	BIGINT FK → users.id	Who follows
// following_id	BIGINT FK → users.id	Who is followed
// created_at	TIMESTAMP DEFAULT NOW()	Date followed

@Table(name = "subscription")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Subscription {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false) // BIGINT FK → users.id
    private User followerId;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false) // BIGINT FK → users.id
    private User followingId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
