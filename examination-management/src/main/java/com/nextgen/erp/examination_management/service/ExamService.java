package com.nextgen.erp.examination_management.service;

import com.nextgen.erp.examination_management.dto.ExamRequest;
import com.nextgen.erp.examination_management.dto.ExamResponse;

import java.util.List;
import java.util.UUID;

public interface ExamService {

    ExamResponse createExam(ExamRequest request);

    ExamResponse updateExam(UUID id, ExamRequest request);

    ExamResponse getExam(UUID id);

    List<ExamResponse> getAllExams();

    List<ExamResponse> getExamsByClass(UUID classId);

    List<ExamResponse> getExamsByAcademicYear(String academicYear);

    void deleteExam(UUID id);
}