package com.nextgen.erp.student_management.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService{
    String uploadProfileImage(MultipartFile file) throws IOException;

    void delete(String fileName) throws IOException;
}
