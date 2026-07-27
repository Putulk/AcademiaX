package com.nextgen.erp.academic_management.AcademicYear.controller;

import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearRequest;
import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearResponse;
import com.nextgen.erp.academic_management.AcademicYear.service.AcademicYearService;
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
@RequestMapping("/api/v1/academic-years")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearService service;

    @Operation(summary = "Create Academic Year")
    @PostMapping
    public ResponseEntity<ApiResponse<AcademicYearResponse>> create(
            @Valid @RequestBody AcademicYearRequest request) {

        AcademicYearResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AcademicYearResponse>builder()
                        .success(true)
                        .message("Academic Year created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public AcademicYearResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<AcademicYearResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public AcademicYearResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody AcademicYearRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/activate")
    public AcademicYearResponse activate(@PathVariable UUID id) {
        return service.activate(id);
    }
}