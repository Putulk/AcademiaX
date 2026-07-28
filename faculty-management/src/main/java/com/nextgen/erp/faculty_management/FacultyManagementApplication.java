package com.nextgen.erp.faculty_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FacultyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacultyManagementApplication.class, args);
    }
}