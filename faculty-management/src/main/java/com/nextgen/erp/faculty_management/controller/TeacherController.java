package com.nextgen.erp.faculty_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.faculty_management.dto.TeacherRequest;
import com.nextgen.erp.faculty_management.dto.TeacherResponse;
import com.nextgen.erp.faculty_management.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER','HR')")
    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(
            @Valid @RequestBody TeacherRequest request) {

        TeacherResponse response = teacherService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TeacherResponse>builder()
                        .success(true)
                        .message("Teacher created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .success(true)
                        .message("Teacher fetched successfully.")
                        .data(teacherService.getById(id))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<TeacherResponse>>builder()
                        .success(true)
                        .message("Teachers fetched successfully.")
                        .data(teacherService.getAll())
                        .build());
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER','HR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TeacherRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .success(true)
                        .message("Teacher updated successfully.")
                        .data(teacherService.update(id, request))
                        .build());
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER','HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        teacherService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Teacher deleted successfully.")
                        .build());
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(teacherService.exists(id))
        );
    }
}