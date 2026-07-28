package com.nextgen.erp.student_management.repository;

import com.nextgen.erp.student_management.entity.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentEnrollmentRepository
        extends JpaRepository<StudentEnrollment, UUID>{

    boolean existsByStudentIdAndAcademicYearId(
            UUID studentId,
            UUID academicYearId);

    boolean existsByClassSectionIdAndRollNumber(
            UUID classSectionId,
            String rollNumber);

    List<StudentEnrollment> findByClassSectionId(UUID classSectionId);

    List<StudentEnrollment> findByStudentId(UUID studentId);
}