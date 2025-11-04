package com._01Blog.backend.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com._01Blog.backend.model.enums.StatusReport;
import com._01Blog.backend.model.enums.TypeReport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 🚨 Report Table
// Column	Type	Description
// id	BIGSERIAL PK	Report ID
// reporter_id	BIGINT FK → users.id	Who reported
// reported_user_id	BIGINT FK → users.id	Who is being reported
// reported_post_id	BIGINT FK → posts.id	Who is being reported
// type 	enum(user, post)
// reason	TEXT	Description/reason
// created_at	TIMESTAMP DEFAULT NOW()	Timestamp
// status	ENUM('PENDING', 'REVIEWED', 'ACTION_TAKEN')	For admin tracking

@Table(name = "report")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Report {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "reporter_id")
    private UUID reporterId;

    @Column(name = "reported_user_id")
    private UUID reportedUserId;

    @Column(name = "reported_post_id")
    private UUID reportedPostId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeReport type;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private StatusReport status;

}
