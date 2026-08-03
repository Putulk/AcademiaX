package com.nextgen.erp.platform_core.mapper;

import com.nextgen.erp.platform_core.dto.request.EntityDefinitionRequest;
import com.nextgen.erp.platform_core.dto.response.EntityDefinitionResponse;
import com.nextgen.erp.platform_core.entity.EntityDefinition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EntityDefinitionMapper {

    public EntityDefinition toEntity(UUID tenantId, EntityDefinitionRequest request) {

        return EntityDefinition.builder()
                .tenantId(tenantId)
                .name(request.name())
                .pluralLabel(request.pluralLabel())
                .description(request.description())
                .displayFieldName(request.displayFieldName())
                .active(request.active())
                .build();
    }

    public EntityDefinitionResponse toResponse(EntityDefinition entity) {

        return EntityDefinitionResponse.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .pluralLabel(entity.getPluralLabel())
                .description(entity.getDescription())
                .displayFieldName(entity.getDisplayFieldName())
                .active(entity.getActive())
                .build();
    }
}
