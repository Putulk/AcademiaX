package com.nextgen.erp.examination_management.client;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "STUDENT-MANAGEMENT-SERVICE")
public interface StudentClient {

    @GetMapping("/api/v1/student-enrollments/{id}/exists")
    ApiResponse<Boolean> studentEnrollmentExists(
            @PathVariable("id") UUID id);
}
