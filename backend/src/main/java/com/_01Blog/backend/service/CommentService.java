package com._01Blog.backend.service;

import org.springframework.stereotype.Service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.CommentDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.CommentRepository;
import com._01Blog.backend.model.repository.PostRepository;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentDto createComment(CommentDto comment, User user) throws ExceptionProgram {
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new ExceptionProgram(400, "post not found"));
    }

}
