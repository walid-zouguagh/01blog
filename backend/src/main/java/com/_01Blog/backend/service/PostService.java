package com._01Blog.backend.service;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.mapper.PostMapper;
import com._01Blog.backend.model.dto.MediaDto;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.PostMedia;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.MediaType;
import com._01Blog.backend.model.repository.MediaTypeRepository;
import com._01Blog.backend.model.repository.PostRepository;
import com._01Blog.backend.util.Upload;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // ← BEST! Auto-injects final fields
public class PostService {

    private final PostRepository postRepository; // ← remove static!
    private final PostMapper postMapper; // ← Spring injects this
    private final MediaTypeRepository mediaTypeRepository;

    @Transactional
    public PostDto save(PostDto postDto, User user) throws ExceptionProgram {

        // 1. Create post entity
        Post post = new Post();
        post.setUser(user);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        // 2. Handle uploaded files
        MultipartFile[] files = postDto.getImages();

        if (files != null && files.length > 5) {
            throw new ExceptionProgram(400, "Maximum 5 files allowed");
        }

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {

                if (file.isEmpty() || file.getOriginalFilename() == null) {
                    continue; // skip empty files
                }

                if (file.getSize() > 100L * 1024 * 1024) {
                    throw new ExceptionProgram(400, "File too big! Max 100MB");
                }

                String fileUrl;
                MediaType type;

                try {
                    fileUrl = Upload.saveImage(file);
                    type = MediaType.IMAGE;

                } catch (Exception e) {
                    try {
                        fileUrl = Upload.saveVideo(file);
                        type = MediaType.VIDEO;
                    } catch (Exception ex) {
                        throw new ExceptionProgram(400, "Failed to process file: " + file.getOriginalFilename());
                    }
                }
                PostMedia media = new PostMedia();
                media.setUrl(fileUrl);
                media.setType(type);
                media.setPost(post);

                post.getMedias().add(media);
            }
        }

        // 3. Save post + medias (cascade does the magic)
        Post savedPost = postRepository.save(post);

        // 4. Return clean DTO
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostDto update(PostDto postDto, User user, String[] deleteImage) throws ExceptionProgram {
        var images = postDto.getImages();
        Post postUpdated = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new ExceptionProgram(400, "Post Don't found"));

        if (postDto.getUser().getId() != user.getId() || postUpdated.getIsHidden()) {
            throw new ExceptionProgram(404, "You cannot update this post.");
        }

        List<PostMedia> medias = mediaTypeRepository.findMediasByPostId(postUpdated.getId());

        if (images != null && deleteImage != null && images.length + medias.size() - deleteImage.length > 5) {
            throw new ExceptionProgram(400, "Maximum 5 files allowed");
        }

        if (images != null && images.length > 0) {
            if (postDto.getMedia() == null) {
                postDto.setMedia(new ArrayList<>());
            }

            for (MultipartFile file : images) {
                if (file.getSize() > 100L * 1024 * 1024) {
                    throw new ExceptionProgram(400, "File too big! Max 100MB");
                }

                String fileUrl;
                MediaType type;

                try {
                    fileUrl = Upload.saveImage(file);
                    type = MediaType.IMAGE;

                } catch (Exception e) {
                    try {
                        fileUrl = Upload.saveVideo(file);
                        type = MediaType.VIDEO;
                    } catch (Exception ex) {
                        throw new ExceptionProgram(400, "Failed to process file: " + file.getOriginalFilename());
                    }
                }
                PostMedia media = new PostMedia();
                media.setUrl(fileUrl);
                media.setType(type);
                media.setPost(postUpdated);

                postUpdated.getMedias().add(media);
            }

            if (deleteImage != null) {
                for (String url : deleteImage) {
                    if (Upload.contain(medias, url)) {
                        mediaTypeRepository.deleteByUrl(url);
                    }
                }
            }

            if (postDto.getMedia() != null) {
                for (MediaDto med : postDto.getMedia()) {
                    mediaTypeRepository.save(null);
                }
            }

        }

    }

    public PostDto findById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
        return postMapper.toDto(post);
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
}