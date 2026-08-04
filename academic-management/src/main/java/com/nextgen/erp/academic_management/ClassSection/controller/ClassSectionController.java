package com.nextgen.erp.academic_management.ClassSection.controller;

import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionRequest;
import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionResponse;
import com.nextgen.erp.academic_management.ClassSection.service.ClassSectionService;
import com.nextgen.erp.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/class-sections")
@RequiredArgsConstructor
public class ClassSectionController {

    private final ClassSectionService service;

    @Operation(summary = "Create Class Section")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClassSectionResponse>> create(
            @Valid @RequestBody ClassSectionRequest request) {

        ClassSectionResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ClassSectionResponse>builder()
                        .success(true)
                        .message("Class Section created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ClassSectionResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ClassSectionResponse> getAll() {
        return service.getAll();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @PutMapping("/{id}")
    public ClassSectionResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody ClassSectionRequest request) {

        return service.update(id, request);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
