package com.nextgen.erp.academic_management.Section.repository;

import com.nextgen.erp.academic_management.Section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SectionRepository
        extends JpaRepository<Section, UUID> {

    boolean existsByName(String name);
}