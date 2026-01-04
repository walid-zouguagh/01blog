package com._01Blog.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.CommentMapper;
import com._01Blog.backend.model.dto.CommentDto;
import com._01Blog.backend.model.entity.Comment;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.repository.CommentRepository;
import com._01Blog.backend.model.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.NonNull;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentDto createComment(CommentDto commentDto, User user) throws ExceptionProgram {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new ExceptionProgram(400, "post not found"));
        
        if (post.isHidden()) {
            throw new ExceptionProgram(400, "you can't comment in this post");
        }

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
        return CommentMapper.toDto(comment);

        
    }

    //Get Comments
    public List<CommentDto> getComments(UUID postId, UUID userId, int offset) {
        return CommentMapper.toDto(commentRepository.getComments(postId, userId, offset));
    }

    // Delete Comment
    @Transactional
    public void deleteComment(User currentUser,@NonNull UUID commentId) throws ExceptionProgram {
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ExceptionProgram(404, "Comment not found"));

        // SECURITY CHECK: Only the owner can delete
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new ExceptionProgram(403, "You can only delete your own comments!");
        }

        // Optional: Also allow post owner to delete any comment
        // if (!comment.getPost().getUser().getId().equals(currentUser.getId())) {
        //     throw new ExceptionProgram(403, "Not authorized");
        // }

        commentRepository.delete(comment);
    }

}
