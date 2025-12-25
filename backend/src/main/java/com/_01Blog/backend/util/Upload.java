package com._01Blog.backend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;
import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.PostMedia;
import com._01Blog.backend.model.enums.MediaType;

import org.apache.tika.mime.MimeType;;

public class Upload {

    private static final String UPLOAD_DIR_IMAGE = "/uploads/images/";
    private static final String UPLOAD_DIR_VIDEO = "/uploads/videos/";

    private static final long MAX_IMAGE_SIZE = 20 * 1024 * 1024; // 20MB
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB

    private static final Tika tika = new Tika();
    private static final MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();

    /**
     * Validate and save image using Tika (most secure way)
     */
    public static String saveImage(MultipartFile file) throws ExceptionProgram {
        if (!isValidImage(file)) {
            throw new ExceptionProgram(400, "Invalid or fake image file");
        }

        return saveFile(file, UPLOAD_DIR_IMAGE, MAX_IMAGE_SIZE);
    }

    /**
     * Validate and save video using Tika
     */
    public static String saveVideo(MultipartFile file) throws ExceptionProgram {
        if (!isValidVideo(file)) {
            throw new ExceptionProgram(400, "Invalid or fake video file");
        }

        return saveFile(file, UPLOAD_DIR_VIDEO, MAX_VIDEO_SIZE);
    }

    /**
     * Check if file is a REAL image using Tika (content-based detection)
     */
    public static boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty())
            return false;

        try {
            if (file.getSize() > MAX_IMAGE_SIZE)
                return false;

            String mimeType = tika.detect(file.getInputStream());
            return mimeType != null && mimeType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Check if file is a REAL video using Tika
     */
    public static boolean isValidVideo(MultipartFile file) {
        if (file == null || file.isEmpty())
            return false;

        try {
            if (file.getSize() > MAX_VIDEO_SIZE)
                return false;

            String mimeType = tika.detect(file.getInputStream());
            return mimeType != null && mimeType.startsWith("video/");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Generic save method (used by image and video)
     */
    private static String saveFile(MultipartFile file, String subDir, long maxSize) throws ExceptionProgram {
        try {
            String projectDir = System.getProperty("user.dir");
            String fullDir = projectDir + subDir;
            Path dirPath = Paths.get(fullDir);

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Get safe extension from Tika (prevents fake .exe → .jpg)
            String safeExtension = getSafeExtension(file);
            String fileName = UUID.randomUUID() + safeExtension;

            Path filePath = dirPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // Return relative URL for frontend
            return subDir + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionProgram(500, "Failed to save file");
        }
    }

    /**
     * Get correct file extension using Tika (very secure!)
     */
    public static String getSafeExtension(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            MimeType type = mimeTypes.forName(mimeType);
            String ext = type.getExtension(); // e.g., ".jpg", ".mp4"

            if (ext != null && !ext.isEmpty()) {
                return ext;
            }
        } catch (Exception ignored) {
        }

        // Fallback
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            return original.substring(original.lastIndexOf(".")).toLowerCase();
        }
        return ".bin"; // unknown → safe
    }

    /**
     * Delete file from disk
     */
    public static void delete(String fileName, MediaType type) {
        if (fileName == null || fileName.isEmpty())
            return;

        String subDir = type == MediaType.IMAGE ? UPLOAD_DIR_IMAGE : UPLOAD_DIR_VIDEO;
        String projectDir = System.getProperty("user.dir");

        Path filePath = Paths.get(projectDir, subDir, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filePath);
            e.printStackTrace();
        }
    }

    public static boolean contain(List<PostMedia> medias, String img) {
        if (medias == null || img == null)
            return false;
        for (PostMedia image : medias) {
            if (image.getUrl().equals(img)) {
                return true;
            }
        }
        return false;
    }

}
