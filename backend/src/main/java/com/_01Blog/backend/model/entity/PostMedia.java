package com._01Blog.backend.model.entity;

import java.util.UUID;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.enums.MediaType;
import com._01Blog.backend.util.Upload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_medias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMedia {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MediaType type; // IMAGE or VIDEO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @PrePersist
    public void delete() throws ExceptionProgram {
        Upload.delete(url, type);
    }
}
