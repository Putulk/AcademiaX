package com.nextgen.erp.attendance_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AttendanceManagementApplication{
    public static void main(String[] args) {
        SpringApplication.run(AttendanceManagementApplication.class, args);
    }
}