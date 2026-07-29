package com.nextgen.erp.academic_management.Section.controller;

import com.nextgen.erp.academic_management.Section.dto.SectionRequest;
import com.nextgen.erp.academic_management.Section.dto.SectionResponse;
import com.nextgen.erp.academic_management.Section.service.SectionService;
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
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService service;

    @Operation(summary = "Create Section")
    @PostMapping
    public ResponseEntity<ApiResponse<SectionResponse>> create(
            @Valid @RequestBody SectionRequest request) {

        SectionResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SectionResponse>builder()
                        .success(true)
                        .message("Section created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SectionResponse>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<SectionResponse>builder()
                        .success(true)
                        .message("Section fetched successfully")
                        .data(service.getById(id))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SectionResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<SectionResponse>>builder()
                        .success(true)
                        .message("Sections fetched successfully")
                        .data(service.getAll())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SectionResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody SectionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<SectionResponse>builder()
                        .success(true)
                        .message("Section updated successfully")
                        .data(service.update(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Section deleted successfully")
                        .build()
        );
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.exists(id))
        );
    }
}
