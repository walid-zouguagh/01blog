package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._01Blog.backend.model.entity.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {
    List<PostMedia> findMediasByPostId(UUID id);

    void deleteByUrl(String url);

    List<PostMedia> findByPostId(UUID id);
}
