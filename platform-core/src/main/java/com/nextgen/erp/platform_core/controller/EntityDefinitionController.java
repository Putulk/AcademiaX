package com.nextgen.erp.platform_core.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.platform_core.dto.request.EntityDefinitionRequest;
import com.nextgen.erp.platform_core.dto.request.FieldDefinitionRequest;
import com.nextgen.erp.platform_core.dto.response.EntityDefinitionResponse;
import com.nextgen.erp.platform_core.dto.response.FieldDefinitionResponse;
import com.nextgen.erp.platform_core.service.EntityDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platform/entity-definitions")
@RequiredArgsConstructor
public class EntityDefinitionController {

    private final EntityDefinitionService entityDefinitionService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<EntityDefinitionResponse>> create(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @Valid @RequestBody EntityDefinitionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityDefinitionService.createDefinition(tenantId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityDefinitionResponse>> get(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(entityDefinitionService.getDefinition(tenantId, id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntityDefinitionResponse>>> list(
            @RequestHeader("X-Tenant-Id") UUID tenantId) {

        return ResponseEntity.ok(
                ApiResponse.success(entityDefinitionService.listDefinitions(tenantId)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityDefinitionResponse>> update(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id,
            @Valid @RequestBody EntityDefinitionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityDefinitionService.updateDefinition(tenantId, id, request)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id) {

        entityDefinitionService.deleteDefinition(tenantId, id);

        return ResponseEntity.ok(ApiResponse.success("Entity definition deleted successfully"));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping("/{id}/fields")
    public ResponseEntity<ApiResponse<FieldDefinitionResponse>> addField(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id,
            @Valid @RequestBody FieldDefinitionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(entityDefinitionService.addField(tenantId, id, request)));
    }

    @GetMapping("/{id}/fields")
    public ResponseEntity<ApiResponse<List<FieldDefinitionResponse>>> listFields(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(entityDefinitionService.listFields(tenantId, id)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping("/{id}/fields/{fieldId}")
    public ResponseEntity<ApiResponse<FieldDefinitionResponse>> updateField(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id,
            @PathVariable UUID fieldId,
            @Valid @RequestBody FieldDefinitionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        entityDefinitionService.updateField(tenantId, id, fieldId, request)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/{id}/fields/{fieldId}")
    public ResponseEntity<ApiResponse<String>> deleteField(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @PathVariable UUID id,
            @PathVariable UUID fieldId) {

        entityDefinitionService.deleteField(tenantId, id, fieldId);

        return ResponseEntity.ok(ApiResponse.success("Field deleted successfully"));
    }
}
