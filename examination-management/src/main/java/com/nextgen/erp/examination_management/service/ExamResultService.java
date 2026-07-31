package com.nextgen.erp.examination_management.service;

import com.nextgen.erp.examination_management.dto.ExamResultRequest;
import com.nextgen.erp.examination_management.dto.ExamResultResponse;

import java.util.List;
import java.util.UUID;

public interface ExamResultService {

    ExamResultResponse createResult(ExamResultRequest request);

    ExamResultResponse updateResult(UUID id, ExamResultRequest request);

    ExamResultResponse getResult(UUID id);

    List<ExamResultResponse> getAllResults();

    List<ExamResultResponse> getResultsByExam(UUID examId);

    List<ExamResultResponse> getResultsByStudentEnrollment(
            UUID studentEnrollmentId);

    List<ExamResultResponse> getResultsBySubject(UUID subjectId);

    List<ExamResultResponse> getResultCard(
            UUID examId, UUID studentEnrollmentId);

    void deleteResult(UUID id);
}
