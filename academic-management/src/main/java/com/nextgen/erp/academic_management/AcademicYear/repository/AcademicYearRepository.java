package com.nextgen.erp.academic_management.AcademicYear.repository;

import com.nextgen.erp.academic_management.AcademicYear.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, UUID> {

    Optional<AcademicYear> findByActiveTrue();

    boolean existsByName(String name);

    Optional<AcademicYear> findByName(String name);
}
