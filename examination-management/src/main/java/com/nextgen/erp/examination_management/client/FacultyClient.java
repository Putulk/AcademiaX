package com.nextgen.erp.examination_management.client;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "FACULTY-MANAGEMENT-SERVICE")
public interface FacultyClient {

    @GetMapping("/api/v1/teachers/{id}/exists")
    ApiResponse<Boolean> teacherExists(
            @PathVariable("id") UUID id);
}