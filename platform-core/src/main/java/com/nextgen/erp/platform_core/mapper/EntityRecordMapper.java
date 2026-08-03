package com.nextgen.erp.platform_core.mapper;

import com.nextgen.erp.platform_core.dto.request.EntityRecordRequest;
import com.nextgen.erp.platform_core.dto.response.EntityRecordResponse;
import com.nextgen.erp.platform_core.entity.EntityRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EntityRecordMapper {

    public EntityRecord toEntity(
            UUID tenantId, UUID entityDefinitionId, EntityRecordRequest request) {

        return EntityRecord.builder()
                .tenantId(tenantId)
                .entityDefinitionId(entityDefinitionId)
                .data(request.data())
                .build();
    }

    public EntityRecordResponse toResponse(EntityRecord entity) {

        return EntityRecordResponse.builder()
                .id(entity.getId())
                .entityDefinitionId(entity.getEntityDefinitionId())
                .data(entity.getData())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
