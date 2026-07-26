package com.nextgen.erp.student_management.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class StorageConfig {

    @Value("${app.storage.location}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
