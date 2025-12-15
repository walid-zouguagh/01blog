package com._01Blog.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PostController {

    private final PostService postService;

    // Users can create/edit/delete posts with media (image or video) and text

    // Create Posts
    @PostMapping(path = "create_post")
    public ResponseEntity<?> createPost(@Valid @ModelAttribute PostDto postDto,
            @RequestAttribute("user") User user) throws ExceptionProgram {
        // System.out.println("you are now in post controller");
        // System.out.println(user.toString());
        PostDto post = postService.save(postDto, user);

        return ResponseEntity.ok(post);
    }

    // Edit Posts
    @PutMapping(path = "edit_post")
    // @PutMapping(path = "/edit_post/{id}")
    public ResponseEntity<?> editPost(@Valid @ModelAttribute PostDto postDto,
            @RequestAttribute("user") User user,
            @RequestParam(name = "deleteImage", defaultValue = "null") String[] deleteImage) throws ExceptionProgram {
        PostDto updated = postService.update(postDto, user, deleteImage);
        return ResponseEntity.ok(updated);
    }

    // Delete Posts
    @DeleteMapping(path = "delete_post")
    public ResponseEntity<?> deletePost(
            @RequestParam(defaultValue = "0", name = "postId") UUID postId,
            @RequestAttribute("user") User user) throws Exception {
        postService.delete(postId, user);
        return ResponseEntity.ok(postId);
    }

    // Get All Posts
    @GetMapping("posts")
    public ResponseEntity<List<PostDto>> getPosts(
            @RequestParam(defaultValue = "0", name = "offset") int offset,
            @RequestAttribute("user") User currentUser) throws ExceptionProgram { // ← you already have this from
                                                                                  // JwtAuthFilter

        List<PostDto> posts = postService.getPosts(currentUser, offset);
        return ResponseEntity.ok(posts);
    }

    // Get posts from following users
    @GetMapping("subscribe-posts")
    public ResponseEntity<List<PostDto>> getSubscribePosts(
            @RequestParam(defaultValue = "0", name = "offset") int offset,
            @RequestAttribute("user") User currentUser) {
        List<PostDto> posts = postService.getSubscribePosts(currentUser, offset);
        return ResponseEntity.ok(posts);
    }

}
