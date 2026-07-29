package com.nextgen.erp.attendance_management.client;

import com.nextgen.erp.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "STUDENT-MANAGEMENT")
public interface StudentClient {

    @GetMapping("/api/v1/enrollments/{id}/exists")
    ApiResponse<Boolean> exists(@PathVariable UUID id);

}