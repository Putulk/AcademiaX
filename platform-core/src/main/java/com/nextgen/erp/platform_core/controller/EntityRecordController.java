package com.nextgen.erp.platform_core.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.platform_core.dto.request.EntityRecordRequest;
import com.nextgen.erp.platform_core.dto.response.EntityRecordResponse;
import com.nextgen.erp.platform_core.service.EntityRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platform/entities/{entityDefinitionId}/records")
@RequiredArgsConstructor
public class EntityRecordController {

    private final EntityRecordService entityRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<EntityRecordResponse>> create(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId,
            @Valid @RequestBody EntityRecordRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityRecordService.create(tenantId, entityDefinitionId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityRecordResponse>> get(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(entityRecordService.get(tenantId, entityDefinitionId, id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntityRecordResponse>>> list(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId) {

        return ResponseEntity.ok(
                ApiResponse.success(entityRecordService.list(tenantId, entityDefinitionId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityRecordResponse>> update(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId,
            @PathVariable UUID id,
            @Valid @RequestBody EntityRecordRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityRecordService.update(tenantId, entityDefinitionId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId,
            @PathVariable UUID id) {

        entityRecordService.delete(tenantId, entityDefinitionId, id);

        return ResponseEntity.ok(ApiResponse.success("Record deleted successfully"));
    }

    @GetMapping("/{id}/label")
    public ResponseEntity<ApiResponse<String>> label(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID entityDefinitionId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityRecordService.resolveLabel(tenantId, entityDefinitionId, id)));
    }
}
