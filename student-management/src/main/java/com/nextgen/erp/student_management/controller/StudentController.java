package com.nextgen.erp.student_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.student_management.dto.StudentRequest;
import com.nextgen.erp.student_management.dto.StudentResponse;
import com.nextgen.erp.student_management.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Create Student")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get Student By Id")
    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable UUID id) {
        return studentService.getById(id);
    }

    @Operation(summary = "Get All Students")
    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @Operation(summary = "Update Student")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PutMapping("/{id}")
    public StudentResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody StudentRequest request) {

        return studentService.update(id, request);
    }

    @Operation(summary = "Delete Student")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        studentService.delete(id);
    }
}