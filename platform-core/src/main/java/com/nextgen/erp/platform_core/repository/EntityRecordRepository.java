package com.nextgen.erp.platform_core.repository;

import com.nextgen.erp.platform_core.entity.EntityRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityRecordRepository extends JpaRepository<EntityRecord, UUID> {

    List<EntityRecord> findByEntityDefinitionIdAndTenantId(UUID entityDefinitionId, UUID tenantId);

    Optional<EntityRecord> findByIdAndEntityDefinitionIdAndTenantId(
            UUID id, UUID entityDefinitionId, UUID tenantId);

    boolean existsByIdAndEntityDefinitionIdAndTenantId(
            UUID id, UUID entityDefinitionId, UUID tenantId);
}
