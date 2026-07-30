package com.nextgen.erp.examination_management.repository;

import com.nextgen.erp.examination_management.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    boolean existsByNameAndAcademicYear(
            String name,
            String academicYear
    );

    List<Exam> findByClassId(UUID classId);

    List<Exam> findByAcademicYear(String academicYear);
}