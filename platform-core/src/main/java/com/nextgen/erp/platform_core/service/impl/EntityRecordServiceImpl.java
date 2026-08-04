package com.nextgen.erp.platform_core.service.impl;

import com.nextgen.erp.platform_core.dto.request.EntityRecordRequest;
import com.nextgen.erp.platform_core.dto.response.EntityRecordResponse;
import com.nextgen.erp.platform_core.entity.EntityDefinition;
import com.nextgen.erp.platform_core.entity.EntityRecord;
import com.nextgen.erp.platform_core.entity.FieldDefinition;
import com.nextgen.erp.platform_core.enums.DataType;
import com.nextgen.erp.platform_core.exception.ResourceNotFoundException;
import com.nextgen.erp.platform_core.exception.ValidationException;
import com.nextgen.erp.platform_core.mapper.EntityRecordMapper;
import com.nextgen.erp.platform_core.repository.EntityDefinitionRepository;
import com.nextgen.erp.platform_core.repository.EntityRecordRepository;
import com.nextgen.erp.platform_core.repository.FieldDefinitionRepository;
import com.nextgen.erp.platform_core.service.EntityRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EntityRecordServiceImpl implements EntityRecordService {

    private final EntityRecordRepository entityRecordRepository;
    private final EntityDefinitionRepository entityDefinitionRepository;
    private final FieldDefinitionRepository fieldDefinitionRepository;
    private final EntityRecordMapper entityRecordMapper;

    @Override
    public EntityRecordResponse create(
            UUID tenantId, UUID entityDefinitionId, EntityRecordRequest request) {

        findDefinition(tenantId, entityDefinitionId);
        validateData(tenantId, entityDefinitionId, request.data());

        EntityRecord record = entityRecordMapper.toEntity(tenantId, entityDefinitionId, request);

        return entityRecordMapper.toResponse(entityRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public EntityRecordResponse get(UUID tenantId, UUID entityDefinitionId, UUID id) {

        return entityRecordMapper.toResponse(findRecord(tenantId, entityDefinitionId, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntityRecordResponse> list(UUID tenantId, UUID entityDefinitionId) {

        findDefinition(tenantId, entityDefinitionId);

        return entityRecordRepository.findByEntityDefinitionIdAndTenantId(entityDefinitionId, tenantId)
                .stream()
                .map(entityRecordMapper::toResponse)
                .toList();
    }

    @Override
    public EntityRecordResponse update(
            UUID tenantId, UUID entityDefinitionId, UUID id, EntityRecordRequest request) {

        EntityRecord record = findRecord(tenantId, entityDefinitionId, id);

        validateData(tenantId, entityDefinitionId, request.data());

        record.setData(request.data());

        return entityRecordMapper.toResponse(entityRecordRepository.save(record));
    }

    @Override
    public void delete(UUID tenantId, UUID entityDefinitionId, UUID id) {

        EntityRecord record = findRecord(tenantId, entityDefinitionId, id);

        entityRecordRepository.delete(record);
    }

    @Override
    @Transactional(readOnly = true)
    public String resolveLabel(UUID tenantId, UUID entityDefinitionId, UUID id) {

        EntityDefinition definition = findDefinition(tenantId, entityDefinitionId);
        EntityRecord record = findRecord(tenantId, entityDefinitionId, id);

        Object value = record.getData().get(definition.getDisplayFieldName());

        return value != null ? value.toString() : record.getId().toString();
    }

    private EntityDefinition findDefinition(UUID tenantId, UUID entityDefinitionId) {

        return entityDefinitionRepository.findByIdAndTenantId(entityDefinitionId, tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Entity definition not found: " + entityDefinitionId));
    }

    private EntityRecord findRecord(UUID tenantId, UUID entityDefinitionId, UUID id) {

        return entityRecordRepository
                .findByIdAndEntityDefinitionIdAndTenantId(id, entityDefinitionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
    }

    private void validateData(UUID tenantId, UUID entityDefinitionId, Map<String, Object> data) {

        List<FieldDefinition> fields =
                fieldDefinitionRepository.findByEntityDefinitionIdOrderByDisplayOrderAsc(entityDefinitionId);

        for (FieldDefinition field : fields) {

            Object value = data.get(field.getName());

            if (Boolean.TRUE.equals(field.getRequired())
                    && (value == null || (value instanceof String s && s.isBlank()))) {

                throw new ValidationException("Field '" + field.getLabel() + "' is required.");
            }

            if (value == null) {
                continue;
            }

            validateType(field, value, tenantId);
        }
    }

    private void validateType(FieldDefinition field, Object value, UUID tenantId) {

        switch (field.getDataType()) {

            case NUMBER -> {
                if (!(value instanceof Number)) {
                    throw new ValidationException(
                            "Field '" + field.getLabel() + "' must be a number.");
                }
            }

            case BOOLEAN -> {
                if (!(value instanceof Boolean)) {
                    throw new ValidationException(
                            "Field '" + field.getLabel() + "' must be true or false.");
                }
            }

            case ENUM -> {
                List<String> options = field.getEnumOptions();
                if (options == null || !options.contains(value.toString())) {
                    throw new ValidationException(
                            "Field '" + field.getLabel() + "' must be one of: " + options);
                }
            }

            case REFERENCE -> {
                UUID referencedId;
                try {
                    referencedId = UUID.fromString(value.toString());
                } catch (IllegalArgumentException e) {
                    throw new ValidationException(
                            "Field '" + field.getLabel() + "' must be a valid record ID.");
                }

                boolean exists = entityRecordRepository.existsByIdAndEntityDefinitionIdAndTenantId(
                        referencedId, field.getReferenceTargetEntityDefinitionId(), tenantId);

                if (!exists) {
                    throw new ValidationException(
                            "Field '" + field.getLabel() + "' references a record that doesn't exist.");
                }
            }

            case TEXT, DATE -> {
                // No stricter check for phase 1 — stored/returned as-is.
            }
        }
    }
}
