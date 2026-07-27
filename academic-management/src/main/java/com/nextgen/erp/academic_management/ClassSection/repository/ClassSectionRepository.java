package com.nextgen.erp.academic_management.ClassSection.repository;

import com.nextgen.erp.academic_management.ClassSection.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassSectionRepository
        extends JpaRepository<ClassSection, UUID>{

    boolean existsByAcademicYearIdAndClassRoomIdAndSectionId(
            UUID academicYearId,
            UUID classRoomId,
            UUID sectionId);
}
