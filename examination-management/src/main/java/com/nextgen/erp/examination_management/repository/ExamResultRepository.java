package com.nextgen.erp.examination_management.repository;

import com.nextgen.erp.examination_management.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamResultRepository
        extends JpaRepository<ExamResult, UUID> {

    boolean existsByExamIdAndStudentEnrollmentIdAndSubjectId(
            UUID examId,
            UUID studentEnrollmentId,
            UUID subjectId
    );

    List<ExamResult> findByExamId(UUID examId);

    List<ExamResult> findByStudentEnrollmentId(UUID studentEnrollmentId);

    List<ExamResult> findBySubjectId(UUID subjectId);

    List<ExamResult> findByExamIdAndStudentEnrollmentId(
            UUID examId,
            UUID studentEnrollmentId
    );
}
