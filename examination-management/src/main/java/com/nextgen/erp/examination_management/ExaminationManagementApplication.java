package com.nextgen.erp.attendance_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExaminationManagementApplication{
    public static void main(String[] args) {
        SpringApplication.run(ExaminationManagementApplication.class, args);
    }
}