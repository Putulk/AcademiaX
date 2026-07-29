package com.nextgen.erp.attendance_management.client;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "ACADEMIC-MANAGEMENT-SERVICE")
public interface AcademicClient {

    @GetMapping("/api/v1/classes/{id}/exists")
    ApiResponse<Boolean> classExists(@PathVariable UUID id);

    @GetMapping("/api/v1/sections/{id}/exists")
    ApiResponse<Boolean> sectionExists(@PathVariable UUID id);

    @GetMapping("/api/v1/subjects/{id}/exists")
    ApiResponse<Boolean> subjectExists(@PathVariable UUID id);

}