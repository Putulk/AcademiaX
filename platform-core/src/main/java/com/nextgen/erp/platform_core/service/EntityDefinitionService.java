package com.nextgen.erp.platform_core.service;

import com.nextgen.erp.platform_core.dto.request.EntityDefinitionRequest;
import com.nextgen.erp.platform_core.dto.request.FieldDefinitionRequest;
import com.nextgen.erp.platform_core.dto.response.EntityDefinitionResponse;
import com.nextgen.erp.platform_core.dto.response.FieldDefinitionResponse;

import java.util.List;
import java.util.UUID;

public interface EntityDefinitionService {

    EntityDefinitionResponse createDefinition(UUID tenantId, EntityDefinitionRequest request);

    EntityDefinitionResponse getDefinition(UUID tenantId, UUID id);

    List<EntityDefinitionResponse> listDefinitions(UUID tenantId);

    EntityDefinitionResponse updateDefinition(UUID tenantId, UUID id, EntityDefinitionRequest request);

    void deleteDefinition(UUID tenantId, UUID id);

    FieldDefinitionResponse addField(UUID tenantId, UUID entityDefinitionId, FieldDefinitionRequest request);

    List<FieldDefinitionResponse> listFields(UUID tenantId, UUID entityDefinitionId);

    FieldDefinitionResponse updateField(
            UUID tenantId, UUID entityDefinitionId, UUID fieldId, FieldDefinitionRequest request);

    void deleteField(UUID tenantId, UUID entityDefinitionId, UUID fieldId);
}
