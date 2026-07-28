package com.nextgen.erp.faculty_management.repository;

import com.nextgen.erp.faculty_management.entity.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeacherAssignmentRepository
        extends JpaRepository<TeacherAssignment, UUID>{

    boolean existsByTeacherIdAndClassSectionIdAndSubjectIdAndAcademicYearId(
            UUID teacherId,
            UUID classSectionId,
            UUID subjectId,
            UUID academicYearId
    );
}
