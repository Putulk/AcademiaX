package com.nextgen.erp.academic_management.ClassRoom.controller;

import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomRequest;
import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomResponse;
import com.nextgen.erp.academic_management.ClassRoom.service.ClassRoomService;
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
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService service;

    @Operation(summary = "Create Class")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClassRoomResponse>> create(
            @Valid @RequestBody ClassRoomRequest request) {

        ClassRoomResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ClassRoomResponse>builder()
                        .success(true)
                        .message("Class created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ClassRoomResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ClassRoomResponse> getAll() {
        return service.getAll();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @PutMapping("/{id}")
    public ClassRoomResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody ClassRoomRequest request) {

        return service.update(id, request);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.exists(id))
        );
    }
}