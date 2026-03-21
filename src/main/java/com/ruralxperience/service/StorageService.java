package com.ruralxperience.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${app.storage.local-dir:uploads}")
    private String localDir;

    @Value("${app.storage.cdn-base-url:http://localhost:8080/files}")
    private String cdnBaseUrl;


    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(localDir));
            log.info("Upload directory ready: {}", localDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String store(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        try {
            Path dest = Paths.get(localDir).resolve(filename);
            Files.copy(file.getInputStream(), dest);
            return cdnBaseUrl + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + filename, e);
        }
    }

    public void delete(String url) {
        String filename = url.replace(cdnBaseUrl + "/", "");
        try {
            Path path = Paths.get(localDir).resolve(filename);
            Files.deleteIfExists(path);
            log.info("Deleted file: {}", filename);
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", filename, e);
        }
    }

    private String sanitize(String filename) {
        if (filename == null) return "upload";
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}