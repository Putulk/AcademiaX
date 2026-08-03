package com.nextgen.erp.platform_core.mapper;

import com.nextgen.erp.platform_core.dto.request.FieldDefinitionRequest;
import com.nextgen.erp.platform_core.dto.response.FieldDefinitionResponse;
import com.nextgen.erp.platform_core.entity.FieldDefinition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FieldDefinitionMapper {

    public FieldDefinition toEntity(UUID entityDefinitionId, FieldDefinitionRequest request) {

        return FieldDefinition.builder()
                .entityDefinitionId(entityDefinitionId)
                .name(request.name())
                .label(request.label())
                .dataType(request.dataType())
                .required(request.required())
                .referenceTargetEntityDefinitionId(request.referenceTargetEntityDefinitionId())
                .enumOptions(request.enumOptions())
                .displayOrder(request.displayOrder() != null ? request.displayOrder() : 0)
                .build();
    }

    public FieldDefinitionResponse toResponse(FieldDefinition entity) {

        return FieldDefinitionResponse.builder()
                .id(entity.getId())
                .entityDefinitionId(entity.getEntityDefinitionId())
                .name(entity.getName())
                .label(entity.getLabel())
                .dataType(entity.getDataType())
                .required(entity.getRequired())
                .referenceTargetEntityDefinitionId(entity.getReferenceTargetEntityDefinitionId())
                .enumOptions(entity.getEnumOptions())
                .displayOrder(entity.getDisplayOrder())
                .build();
    }
}
