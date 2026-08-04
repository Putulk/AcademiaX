package com.nextgen.erp.platform_core.service.impl;

import com.nextgen.erp.platform_core.dto.request.EntityDefinitionRequest;
import com.nextgen.erp.platform_core.dto.request.FieldDefinitionRequest;
import com.nextgen.erp.platform_core.dto.response.EntityDefinitionResponse;
import com.nextgen.erp.platform_core.dto.response.FieldDefinitionResponse;
import com.nextgen.erp.platform_core.entity.EntityDefinition;
import com.nextgen.erp.platform_core.entity.FieldDefinition;
import com.nextgen.erp.platform_core.enums.DataType;
import com.nextgen.erp.platform_core.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.platform_core.exception.ResourceNotFoundException;
import com.nextgen.erp.platform_core.exception.ValidationException;
import com.nextgen.erp.platform_core.mapper.EntityDefinitionMapper;
import com.nextgen.erp.platform_core.mapper.FieldDefinitionMapper;
import com.nextgen.erp.platform_core.repository.EntityDefinitionRepository;
import com.nextgen.erp.platform_core.repository.FieldDefinitionRepository;
import com.nextgen.erp.platform_core.service.EntityDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EntityDefinitionServiceImpl implements EntityDefinitionService {

    private final EntityDefinitionRepository entityDefinitionRepository;
    private final FieldDefinitionRepository fieldDefinitionRepository;
    private final EntityDefinitionMapper entityDefinitionMapper;
    private final FieldDefinitionMapper fieldDefinitionMapper;

    @Override
    public EntityDefinitionResponse createDefinition(UUID tenantId, EntityDefinitionRequest request) {

        if (entityDefinitionRepository.existsByTenantIdAndName(tenantId, request.name())) {
            throw new ResourceAlreadyExistsException(
                    "An entity named '" + request.name() + "' already exists for this tenant.");
        }

        EntityDefinition definition = entityDefinitionMapper.toEntity(tenantId, request);

        return entityDefinitionMapper.toResponse(entityDefinitionRepository.save(definition));
    }

    @Override
    @Transactional(readOnly = true)
    public EntityDefinitionResponse getDefinition(UUID tenantId, UUID id) {

        return entityDefinitionMapper.toResponse(findDefinition(tenantId, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntityDefinitionResponse> listDefinitions(UUID tenantId) {

        return entityDefinitionRepository.findByTenantId(tenantId)
                .stream()
                .map(entityDefinitionMapper::toResponse)
                .toList();
    }

    @Override
    public EntityDefinitionResponse updateDefinition(
            UUID tenantId, UUID id, EntityDefinitionRequest request) {

        EntityDefinition definition = findDefinition(tenantId, id);

        definition.setName(request.name());
        definition.setPluralLabel(request.pluralLabel());
        definition.setDescription(request.description());
        definition.setDisplayFieldName(request.displayFieldName());
        definition.setActive(request.active());

        return entityDefinitionMapper.toResponse(entityDefinitionRepository.save(definition));
    }

    @Override
    public void deleteDefinition(UUID tenantId, UUID id) {

        EntityDefinition definition = findDefinition(tenantId, id);

        fieldDefinitionRepository.deleteByEntityDefinitionId(definition.getId());
        entityDefinitionRepository.delete(definition);
    }

    @Override
    public FieldDefinitionResponse addField(
            UUID tenantId, UUID entityDefinitionId, FieldDefinitionRequest request) {

        findDefinition(tenantId, entityDefinitionId);
        validateFieldRequest(request);

        FieldDefinition field = fieldDefinitionMapper.toEntity(entityDefinitionId, request);

        return fieldDefinitionMapper.toResponse(fieldDefinitionRepository.save(field));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FieldDefinitionResponse> listFields(UUID tenantId, UUID entityDefinitionId) {

        findDefinition(tenantId, entityDefinitionId);

        return fieldDefinitionRepository
                .findByEntityDefinitionIdOrderByDisplayOrderAsc(entityDefinitionId)
                .stream()
                .map(fieldDefinitionMapper::toResponse)
                .toList();
    }

    @Override
    public FieldDefinitionResponse updateField(
            UUID tenantId, UUID entityDefinitionId, UUID fieldId, FieldDefinitionRequest request) {

        findDefinition(tenantId, entityDefinitionId);
        validateFieldRequest(request);

        FieldDefinition field = fieldDefinitionRepository
                .findByIdAndEntityDefinitionId(fieldId, entityDefinitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fieldId));

        field.setName(request.name());
        field.setLabel(request.label());
        field.setDataType(request.dataType());
        field.setRequired(request.required());
        field.setReferenceTargetEntityDefinitionId(request.referenceTargetEntityDefinitionId());
        field.setEnumOptions(request.enumOptions());
        field.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : 0);

        return fieldDefinitionMapper.toResponse(fieldDefinitionRepository.save(field));
    }

    @Override
    public void deleteField(UUID tenantId, UUID entityDefinitionId, UUID fieldId) {

        findDefinition(tenantId, entityDefinitionId);

        FieldDefinition field = fieldDefinitionRepository
                .findByIdAndEntityDefinitionId(fieldId, entityDefinitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fieldId));

        fieldDefinitionRepository.delete(field);
    }

    private EntityDefinition findDefinition(UUID tenantId, UUID id) {

        return entityDefinitionRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity definition not found: " + id));
    }

    private void validateFieldRequest(FieldDefinitionRequest request) {

        if (request.dataType() == DataType.REFERENCE
                && request.referenceTargetEntityDefinitionId() == null) {

            throw new ValidationException(
                    "A REFERENCE field requires a referenceTargetEntityDefinitionId.");
        }

        if (request.dataType() == DataType.ENUM
                && (request.enumOptions() == null || request.enumOptions().isEmpty())) {

            throw new ValidationException("An ENUM field requires at least one enum option.");
        }
    }
}
