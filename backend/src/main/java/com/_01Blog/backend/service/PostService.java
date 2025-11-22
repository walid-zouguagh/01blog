package com._01Blog.backend.service;

import org.springframework.stereotype.Service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.PostRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor

public class PostService {
    private static PostRepository postRepository;

    public Post save(PostDto post, User user) throws ExceptionProgram {
        // Get Images
        var images = post.getImages();

        return null;
    }
}
