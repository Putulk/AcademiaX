package com.nextgen.erp.academic_management.Subject.controller;

import com.nextgen.erp.academic_management.Subject.dto.SubjectRequest;
import com.nextgen.erp.academic_management.Subject.dto.SubjectResponse;
import com.nextgen.erp.academic_management.Subject.service.SubjectService;
import com.nextgen.erp.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(
            summary = "Create Subject",
            description = "Creates a new subject"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> create(
            @Valid @RequestBody SubjectRequest request) {

        SubjectResponse response = subjectService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubjectResponse>builder()
                        .success(true)
                        .message("Subject created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get Subject By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getById(
            @PathVariable UUID id) {

        SubjectResponse response = subjectService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .success(true)
                        .message("Subject fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get All Subjects")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAll() {

        List<SubjectResponse> response = subjectService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<SubjectResponse>>builder()
                        .success(true)
                        .message("Subjects fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Update Subject")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody SubjectRequest request) {

        SubjectResponse response = subjectService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .success(true)
                        .message("Subject updated successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Delete Subject")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        subjectService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Subject deleted successfully")
                        .build()
        );
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(subjectService.exists(id))
        );
    }
}
