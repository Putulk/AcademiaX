package com.nextgen.erp.academic_management.Subject.repository;

import com.nextgen.erp.academic_management.Subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository
        extends JpaRepository<Subject, UUID>{

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
