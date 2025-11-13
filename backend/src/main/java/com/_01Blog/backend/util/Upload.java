package com._01Blog.backend.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

import com._01Blog.backend.exception.ExceptionProgram;

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

}
