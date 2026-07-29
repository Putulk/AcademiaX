package com.nextgen.erp.student_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.student_management.dto.StudentEnrollmentRequest;
import com.nextgen.erp.student_management.dto.StudentEnrollmentResponse;
import com.nextgen.erp.student_management.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student-enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final StudentEnrollmentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentEnrollmentResponse>> create(
            @Valid @RequestBody StudentEnrollmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<StudentEnrollmentResponse>builder()
                        .success(true)
                        .message("Student enrolled successfully.")
                        .data(service.create(request))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollmentResponse>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<StudentEnrollmentResponse>builder()
                        .success(true)
                        .message("Enrollment fetched successfully.")
                        .data(service.getById(id))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentEnrollmentResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<StudentEnrollmentResponse>>builder()
                        .success(true)
                        .message("Enrollments fetched successfully.")
                        .data(service.getAll())
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollmentResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody StudentEnrollmentRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<StudentEnrollmentResponse>builder()
                        .success(true)
                        .message("Enrollment updated successfully.")
                        .data(service.update(id, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Enrollment deleted successfully.")
                        .build());
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(studentEnrollmentService.exists(id))
        );
    }
}