package com.nextgen.erp.user_management.service.impl;

import com.nextgen.erp.user_management.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService{

    @Value("${app.storage.location}")
    private String uploadDir;

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException{

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), path);

        return fileName;
    }

    @Override
    public void delete(String fileName) throws IOException{
        Files.deleteIfExists(Paths.get(uploadDir, fileName));
    }
}