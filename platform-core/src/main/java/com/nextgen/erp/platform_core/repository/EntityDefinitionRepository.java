package com.nextgen.erp.platform_core.repository;

import com.nextgen.erp.platform_core.entity.EntityDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityDefinitionRepository extends JpaRepository<EntityDefinition, UUID> {

    List<EntityDefinition> findByTenantId(UUID tenantId);

    Optional<EntityDefinition> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByTenantIdAndName(UUID tenantId, String name);
}
