package com._01Blog.backend.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.dto.MediaDto;
import com._01Blog.backend.model.dto.PostDto;
import com._01Blog.backend.model.entity.Post;
import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.MediaType;
import com._01Blog.backend.model.repository.PostRepository;
import com._01Blog.backend.util.Upload;

import jakarta.mail.Multipart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor

public class PostService {
    private static PostRepository postRepository;

    public Post save(PostDto postDto, User user) throws ExceptionProgram {
        // Get Images
        var images = postDto.getImages();
        if (images != null && images.length > 5) {
            throw new ExceptionProgram(400, "5 media items are allowed.");
        }

        if (images != null && images.length > 0) {
            if (postDto.getMedia() == null) {
                postDto.setMedia(new ArrayList<>());
            }

            for (MultipartFile file : images) {
                if (file.getSize() > 50 * 1024 * 1024) {
                    throw new ExceptionProgram(400, null);
                }

                String fileName;
                MediaType type = null;
                try {
                    Upload.isValidPhoto(file);
                    fileName = Upload.saveImage(file);
                    type = type.IMAGE;

                } catch (Exception e) {
                    if (Upload.isValidVideo(file)) {
                        fileName = Upload.saveVideo(file);
                        type = type.VIDEO;
                    } else {
                        throw new ExceptionProgram(400, "invalid file");
                    }
                }
                postDto.getMedia().add(new MediaDto(fileName, type));

            }
        }

        return null;
    }
}
