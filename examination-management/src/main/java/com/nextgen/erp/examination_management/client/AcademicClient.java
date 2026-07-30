package com.nextgen.erp.examination_management.client;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ACADEMIC-MANAGEMENT")
public interface AcademicClient {

    @GetMapping("/api/v1/classes/{id}/exists")
    ApiResponse<Boolean> classExists(
            @PathVariable("id") UUID id);

    @GetMapping("/api/v1/subjects/{id}/exists")
    ApiResponse<Boolean> subjectExists(
            @PathVariable("id") UUID id);

    @GetMapping("/api/v1/sections/{id}/exists")
    ApiResponse<Boolean> sectionExists(
            @PathVariable("id") UUID id);
}