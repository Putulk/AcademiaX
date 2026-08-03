package com.nextgen.erp.platform_core.service;

import com.nextgen.erp.platform_core.dto.request.EntityRecordRequest;
import com.nextgen.erp.platform_core.dto.response.EntityRecordResponse;

import java.util.List;
import java.util.UUID;

public interface EntityRecordService {

    EntityRecordResponse create(UUID tenantId, UUID entityDefinitionId, EntityRecordRequest request);

    EntityRecordResponse get(UUID tenantId, UUID entityDefinitionId, UUID id);

    List<EntityRecordResponse> list(UUID tenantId, UUID entityDefinitionId);

    EntityRecordResponse update(
            UUID tenantId, UUID entityDefinitionId, UUID id, EntityRecordRequest request);

    void delete(UUID tenantId, UUID entityDefinitionId, UUID id);

    String resolveLabel(UUID tenantId, UUID entityDefinitionId, UUID id);
}
