package com._01Blog.backend.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.tomcat.util.http.parser.MediaType;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor

// 📝 Post Table
// Column	Type	Description
// id	BIGSERIAL PK	Post ID
// user_id	BIGINT FK → users.id	Author
// content	TEXT	Post text or markdown
// media_url	VARCHAR(255)	Image or video URL
// created_at	TIMESTAMP DEFAULT NOW()	Date created
// updated_at	TIMESTAMP	Last update
// is_hidden	BOOLEAN DEFAULT FALSE	Admin moderation

public class Post {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "media_type")
    private MediaType mediaType;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;
}
