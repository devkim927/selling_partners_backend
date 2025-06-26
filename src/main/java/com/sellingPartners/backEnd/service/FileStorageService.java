package com.sellingPartners.backEnd.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 파일 저장 메서드
    public String storeFile(MultipartFile file, String subDir) throws Exception {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file");

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + extension;
        
        Path targetDir = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
        Files.createDirectories(targetDir);
        
        Path targetPath = targetDir.resolve(newFileName);
        file.transferTo(targetPath.toFile());
        
        return "/uploads/" + subDir + "/" + newFileName;
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            // URL에서 실제 파일 경로 추출
            String relativePath = fileUrl.replaceFirst("^/uploads/", "");
            Path filePath = Paths.get(uploadDir, relativePath);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }
}
