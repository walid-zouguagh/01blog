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

@Table(name = "comment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

// 💬 Comment Table
// Column Type Description
// id BIGSERIAL PK Comment ID
// post_id BIGINT FK → posts.id Related post
// user_id BIGINT FK → users.id Author
// content TEXT Comment text
// created_at TIMESTAMP DEFAULT NOW() Timestamp

public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false) // BIGINT FK → posts.id Related post
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // BIGINT FK → users.id
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
