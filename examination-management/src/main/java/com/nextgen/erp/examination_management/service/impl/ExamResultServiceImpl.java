package com.nextgen.erp.examination_management.service.impl;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.examination_management.client.AcademicClient;
import com.nextgen.erp.examination_management.client.StudentClient;
import com.nextgen.erp.examination_management.dto.ExamResultRequest;
import com.nextgen.erp.examination_management.dto.ExamResultResponse;
import com.nextgen.erp.examination_management.entity.ExamResult;
import com.nextgen.erp.examination_management.enums.Grade;
import com.nextgen.erp.examination_management.enums.ResultStatus;
import com.nextgen.erp.examination_management.mapper.ExamResultMapper;
import com.nextgen.erp.examination_management.repository.ExamRepository;
import com.nextgen.erp.examination_management.repository.ExamResultRepository;
import com.nextgen.erp.examination_management.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final ExamResultMapper examResultMapper;
    private final ExamRepository examRepository;
    private final AcademicClient academicClient;
    private final StudentClient studentClient;

    @Override
    public ExamResultResponse createResult(ExamResultRequest request) {

        validateRequest(request);

        if (examResultRepository
                .existsByExamIdAndStudentEnrollmentIdAndSubjectId(
                        request.examId(),
                        request.studentEnrollmentId(),
                        request.subjectId())) {

            throw new RuntimeException(
                    "Result already exists for this student in this subject for this exam."
            );
        }

        ExamResult result = examResultMapper.toEntity(request);

        applyMarksAndGrade(result, request);

        result = examResultRepository.save(result);

        return examResultMapper.toResponse(result);
    }

    @Override
    public ExamResultResponse updateResult(
            UUID id,
            ExamResultRequest request) {

        ExamResult existing = examResultRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam result not found."));

        validateRequest(request);

        existing.setExamId(request.examId());
        existing.setStudentEnrollmentId(request.studentEnrollmentId());
        existing.setSubjectId(request.subjectId());
        existing.setMaxMarks(request.maxMarks());

        applyMarksAndGrade(existing, request);

        return examResultMapper.toResponse(
                examResultRepository.save(existing)
        );
    }

    private void validateRequest(ExamResultRequest request) {

        // 1. Validate exam
        examRepository.findById(request.examId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Exam not found: " + request.examId()));

        // 2. Validate student enrollment
        ApiResponse<Boolean> enrollmentResponse =
                studentClient.studentEnrollmentExists(
                        request.studentEnrollmentId());

        if (!Boolean.TRUE.equals(enrollmentResponse.getData())) {

            throw new RuntimeException(
                    "Student enrollment not found: "
                            + request.studentEnrollmentId());
        }

        // 3. Validate subject
        ApiResponse<Boolean> subjectResponse =
                academicClient.subjectExists(request.subjectId());

        if (!Boolean.TRUE.equals(subjectResponse.getData())) {

            throw new RuntimeException(
                    "Subject not found: " + request.subjectId());
        }

        // 4. Validate marks
        if (!request.absent()
                && request.marksObtained() > request.maxMarks()) {

            throw new RuntimeException(
                    "Marks obtained cannot exceed maximum marks."
            );
        }
    }

    private void applyMarksAndGrade(
            ExamResult result,
            ExamResultRequest request) {

        double marksObtained = request.absent()
                ? 0.0
                : request.marksObtained();

        double percentage =
                (marksObtained / request.maxMarks()) * 100;

        Grade grade = request.absent()
                ? Grade.F
                : Grade.fromPercentage(percentage);

        ResultStatus status = grade == Grade.F
                ? ResultStatus.FAIL
                : ResultStatus.PASS;

        result.setMarksObtained(marksObtained);
        result.setGrade(grade);
        result.setResultStatus(status);
        result.setAbsent(request.absent());
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResultResponse getResult(UUID id) {

        ExamResult result = examResultRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam result not found."));

        return examResultMapper.toResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getAllResults() {

        return examResultRepository.findAll()
                .stream()
                .map(examResultMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getResultsByExam(UUID examId) {

        return examResultRepository.findByExamId(examId)
                .stream()
                .map(examResultMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getResultsByStudentEnrollment(
            UUID studentEnrollmentId) {

        return examResultRepository
                .findByStudentEnrollmentId(studentEnrollmentId)
                .stream()
                .map(examResultMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getResultsBySubject(UUID subjectId) {

        return examResultRepository.findBySubjectId(subjectId)
                .stream()
                .map(examResultMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getResultCard(
            UUID examId, UUID studentEnrollmentId) {

        return examResultRepository
                .findByExamIdAndStudentEnrollmentId(
                        examId, studentEnrollmentId)
                .stream()
                .map(examResultMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteResult(UUID id) {

        if (!examResultRepository.existsById(id)) {
            throw new RuntimeException("Exam result not found.");
        }

        examResultRepository.deleteById(id);
    }
}
