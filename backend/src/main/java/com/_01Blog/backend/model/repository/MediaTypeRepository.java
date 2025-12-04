package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._01Blog.backend.model.entity.PostMedia;
import com._01Blog.backend.model.enums.MediaType;

public interface MediaTypeRepository extends JpaRepository<MediaType, UUID> {
    List<PostMedia> findMediasByPostId(UUID id);

    void deleteByUrl(String url);
}
