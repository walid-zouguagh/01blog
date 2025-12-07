package com._01Blog.backend.model.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findById(UUID id);

}
