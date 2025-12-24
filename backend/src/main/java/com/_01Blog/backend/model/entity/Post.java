package com._01Blog.backend.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;

    // THIS IS THE MAGIC: One post → Many medias
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMedia> medias = new ArrayList<>();

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setUrl(String url) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUrl'");
    }
}

// ==================================

// package com._01Blog.backend.model.entity;

// import java.time.LocalDateTime;
// import java.util.UUID;

// import org.hibernate.annotations.UuidGenerator;

// import com._01Blog.backend.model.enums.MediaType;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "post")
// @Data
// @AllArgsConstructor
// @NoArgsConstructor

// public class Post {
// @Id
// @GeneratedValue(generator = "UUID")
// @UuidGenerator
// private UUID id;

// @ManyToOne
// @JoinColumn(name = "user_id", nullable = false)
// private User user;

// @Column(name = "title", nullable = false)
// private String title;

// @Column(name = "content")
// private String content;

// // @Enumerated(EnumType.STRING)
// // @Column(name = "media_type")
// // private MediaType mediaType;

// // @Column(name = "media_url")
// // private String mediaUrl;

// @Column(name = "created_at")
// private LocalDateTime createdAt;

// @Column(name = "updated_at")
// private LocalDateTime updatedAt;

// @Column(name = "is_hidden")
// private Boolean isHidden = false;

// @PrePersist
// protected void onCreate() {
// createdAt = LocalDateTime.now();
// }

// @PreUpdate
// protected void onUpdate() {
// updatedAt = LocalDateTime.now();
// }

// }
