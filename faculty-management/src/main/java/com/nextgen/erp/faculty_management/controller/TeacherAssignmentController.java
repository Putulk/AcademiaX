package com.nextgen.erp.faculty_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.faculty_management.dto.TeacherAssignmentRequest;
import com.nextgen.erp.faculty_management.dto.TeacherAssignmentResponse;
import com.nextgen.erp.faculty_management.service.TeacherAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher-assignments")
@RequiredArgsConstructor
public class TeacherAssignmentController {

    private final TeacherAssignmentService teacherAssignmentService;

    @Operation(
            summary = "Create Teacher Assignment",
            description = "Assign a teacher to a subject, class section and academic year."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<TeacherAssignmentResponse>> create(
            @Valid @RequestBody TeacherAssignmentRequest request) {

        TeacherAssignmentResponse response =
                teacherAssignmentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TeacherAssignmentResponse>builder()
                        .success(true)
                        .message("Teacher assignment created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get Teacher Assignment by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherAssignmentResponse>> getById(
            @PathVariable UUID id) {

        TeacherAssignmentResponse response =
                teacherAssignmentService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<TeacherAssignmentResponse>builder()
                        .success(true)
                        .message("Teacher assignment fetched successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get All Teacher Assignments")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherAssignmentResponse>>> getAll() {

        List<TeacherAssignmentResponse> response =
                teacherAssignmentService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<TeacherAssignmentResponse>>builder()
                        .success(true)
                        .message("Teacher assignments fetched successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Update Teacher Assignment")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherAssignmentResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TeacherAssignmentRequest request) {

        TeacherAssignmentResponse response =
                teacherAssignmentService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<TeacherAssignmentResponse>builder()
                        .success(true)
                        .message("Teacher assignment updated successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Delete Teacher Assignment")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        teacherAssignmentService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Teacher assignment deleted successfully")
                        .build());
    }
}