package com._01Blog.backend.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.exception.ExceptionProgram;
import com._01Blog.backend.model.entity.PostMedia;
import com._01Blog.backend.model.enums.MediaType;

public class Upload {
    private static final String UPLOAD_DIR_IMAGE = "/uploads/images/";
    private static final String UPLOAD_DIR_VIDEO = "/uploads/videos";

    public static boolean isValidPhoto(MultipartFile file) throws ExceptionProgram {
        if (file == null || file.isEmpty()) {
            throw new ExceptionProgram(400, "image is empty");
        }

        // header : content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ExceptionProgram(400, "invalid image");
        }

        // body : bytes
        try {
            byte[] bytes = file.getBytes();
            if (!isValidImage(bytes)) {
                throw new ExceptionProgram(400, "image is invalid");
            }
            return true;
        } catch (Exception e) {
            throw new ExceptionProgram(400, "failed to read image");
        }

    }

    public static boolean isValidImage(byte[] bytes) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            return image != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        }

        int indexOfDot = fileName.lastIndexOf(".");
        return (indexOfDot >= 0) ? fileName.substring(indexOfDot) : "";

    }

    public static String saveImage(MultipartFile file) throws ExceptionProgram {
        isValidPhoto(file);
        String dirBackendString = System.getProperty("user.dir");
        File dir = new File(dirBackendString, UPLOAD_DIR_IMAGE);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + (extension.isEmpty() ? ".jpg" : extension);

        File destination = new File(dir, fileName);

        try {
            file.transferTo(destination);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionProgram(500, "failed to save image");
        }
        return fileName;
    }

    public static boolean isValidVideo(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                return false;
            }

            byte[] bytes = file.getBytes();
            String header = new String(bytes, 0, Math.min(bytes.length, 64));
            return header.contains("ftyp") || header.contains("moov");

        } catch (Exception e) {
            return false;
        }

    }

    public static String saveVideo(MultipartFile file) throws ExceptionProgram {
        if (!isValidVideo(file)) {
            throw new ExceptionProgram(400, "file is not a video");
        }

        String dirBackendString = System.getProperty("user.dir"); // "Give me the full path of the folder where my Java
                                                                  // app is running right now"
        File dir = new File(dirBackendString, UPLOAD_DIR_VIDEO);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String extention = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + (extention.isEmpty() ? ".mp4" : extention);
        File destination = new File(dir, fileName);

        try {
            file.transferTo(destination);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionProgram(500, "failed to save video");
        }
        return fileName;
    }

    public static void delete(String file, MediaType type) throws ExceptionProgram {
        if (file == null || file.isEmpty()) {
            return;
        }

        String dirBackendString = System.getProperty("user.dir");
        String baseDir;

        if ("image".equalsIgnoreCase(type.IMAGE.toString())) {
            baseDir = UPLOAD_DIR_IMAGE;
        } else if ("video".equalsIgnoreCase(type.VIDEO.toString())) {
            baseDir = UPLOAD_DIR_VIDEO;
        } else {
            throw new ExceptionProgram(500, "unknown file type" + type);
        }

        Path filePath = Paths.get(dirBackendString, baseDir, file);

        try {
            Files.deleteIfExists(filePath);
            System.out.println("Delete file: " + filePath);
        } catch (Exception e) {
            System.out.println("Failed to delete file: " + filePath);
            e.printStackTrace();
        }
    }

    public static boolean contain(List<PostMedia> medias, String img) {
        if (medias == null || img == null) return false;
        for (PostMedia image : medias) {
            if (image.getUrl().equals(img)) {
                return true;
            }
        }
        return false;
    }

}
