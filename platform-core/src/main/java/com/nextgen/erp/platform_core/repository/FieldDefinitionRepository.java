package com.nextgen.erp.platform_core.repository;

import com.nextgen.erp.platform_core.entity.FieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldDefinitionRepository extends JpaRepository<FieldDefinition, UUID> {

    List<FieldDefinition> findByEntityDefinitionIdOrderByDisplayOrderAsc(UUID entityDefinitionId);

    Optional<FieldDefinition> findByIdAndEntityDefinitionId(UUID id, UUID entityDefinitionId);

    void deleteByEntityDefinitionId(UUID entityDefinitionId);
}
