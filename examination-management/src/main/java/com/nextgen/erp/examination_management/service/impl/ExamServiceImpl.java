package com.nextgen.erp.examination_management.service.impl;

import com.nextgen.erp.examination_management.client.AcademicClient;
import com.nextgen.erp.examination_management.dto.ExamRequest;
import com.nextgen.erp.examination_management.dto.ExamResponse;
import com.nextgen.erp.examination_management.entity.Exam;
import com.nextgen.erp.examination_management.mapper.ExamMapper;
import com.nextgen.erp.examination_management.repository.ExamRepository;
import com.nextgen.erp.examination_management.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final AcademicClient academicClient;

    @Override
    public ExamResponse createExam(ExamRequest request) {

        // Validate Class
        if (!Boolean.TRUE.equals(
                academicClient.classExists(request.classId()).getData())) {

            throw new RuntimeException(
                    "Class not found: " + request.classId()
            );
        }

        // Validate dates
        if (request.endDate().isBefore(request.startDate())) {
            throw new RuntimeException(
                    "End date cannot be before start date."
            );
        }

        // Check duplicate exam
        if (examRepository.existsByNameAndAcademicYear(
                request.name(),
                request.academicYear())) {

            throw new RuntimeException(
                    "Exam already exists for this academic year."
            );
        }

        Exam exam = examMapper.toEntity(request);

        exam = examRepository.save(exam);

        return examMapper.toResponse(exam);
    }

    @Override
    public ExamResponse updateExam(
            UUID id,
            ExamRequest request) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found."));

        if (request.endDate().isBefore(request.startDate())) {
            throw new RuntimeException(
                    "End date cannot be before start date."
            );
        }

        exam.setName(request.name());
        exam.setAcademicYear(request.academicYear());
        exam.setClassId(request.classId());
        exam.setStartDate(request.startDate());
        exam.setEndDate(request.endDate());
        exam.setStatus(request.status());
        exam.setDescription(request.description());

        return examMapper.toResponse(
                examRepository.save(exam)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getExam(UUID id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found."));

        return examMapper.toResponse(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAllExams() {

        return examRepository.findAll()
                .stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getExamsByClass(UUID classId) {

        return examRepository.findByClassId(classId)
                .stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getExamsByAcademicYear(
            String academicYear) {

        return examRepository.findByAcademicYear(academicYear)
                .stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteExam(UUID id) {

        if (!examRepository.existsById(id)) {
            throw new RuntimeException("Exam not found.");
        }

        examRepository.deleteById(id);
    }
}