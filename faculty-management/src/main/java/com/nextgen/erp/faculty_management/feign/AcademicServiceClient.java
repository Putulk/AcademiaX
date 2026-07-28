package com.nextgen.erp.faculty_management.feign;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.faculty_management.dto.AcademicYearResponse;
import com.nextgen.erp.faculty_management.dto.ClassSectionResponse;
import com.nextgen.erp.faculty_management.dto.SubjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "academic-service",
        url = "${services.academic.url}"
)
public interface AcademicServiceClient {

    @GetMapping("/api/v1/subjects/{id}")
    ApiResponse<SubjectResponse> getSubject(
            @PathVariable UUID id);

    @GetMapping("/api/v1/class-sections/{id}")
    ApiResponse<ClassSectionResponse> getClassSection(
            @PathVariable UUID id);

    @GetMapping("/api/v1/academic-years/{id}")
    ApiResponse<AcademicYearResponse> getAcademicYear(
            @PathVariable UUID id);
}
