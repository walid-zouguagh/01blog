package com._01Blog.backend.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.LikeRepository;
import com._01Blog.backend.model.repository.PostRepository;

// @Service
// public class LikeService {
//     private final LikeRepository likeRepository;
//     private final PostRepository postRepository;

//     @Transactional
//     public Map<String, Object> like(User user, UUID postId) throws ExceptionProgram {
//         if (postId == null) {
//             throw new ExceptionProgram(400, "post not found");               
//         }
//         Post post = postRepository.findById(postId)
//                     .orElseThrow(() -> new ExceptionProgram(400, "this post not found"));

//         if (post.isHidden()){
//             new ExceptionProgram(400, "You can’t like this post");
//         }

//         // verify is liked 
//         boolean isLiked = likeRepository.isLiked(user.getId(), postId);
//     }

// }
