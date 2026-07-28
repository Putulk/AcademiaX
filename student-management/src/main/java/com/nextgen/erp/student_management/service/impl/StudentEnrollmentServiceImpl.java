package com.nextgen.erp.student_management.service.impl;

import com.nextgen.erp.student_management.dto.StudentEnrollmentRequest;
import com.nextgen.erp.student_management.dto.StudentEnrollmentResponse;
import com.nextgen.erp.student_management.entity.Student;
import com.nextgen.erp.student_management.entity.StudentEnrollment;
import com.nextgen.erp.student_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.student_management.exception.ResourceNotFoundException;
import com.nextgen.erp.student_management.mapper.StudentEnrollmentMapper;
import com.nextgen.erp.student_management.repository.StudentEnrollmentRepository;
import com.nextgen.erp.student_management.repository.StudentRepository;
import com.nextgen.erp.student_management.service.StudentEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl
        implements StudentEnrollmentService {

    private final StudentEnrollmentRepository repository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentMapper mapper;

    @Override
    public StudentEnrollmentResponse create(StudentEnrollmentRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        if (repository.existsByStudentIdAndAcademicYearId(
                request.getStudentId(),
                request.getAcademicYearId())) {

            throw new ResourceAlreadyExistsException(
                    "Student already enrolled for this academic year.");
        }

        if (repository.existsByClassSectionIdAndRollNumber(
                request.getClassSectionId(),
                request.getRollNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Roll Number already exists in this class.");
        }

        StudentEnrollment enrollment =
                mapper.toEntity(request);

        enrollment = repository.save(enrollment);

        return mapper.toResponse(enrollment);
    }

    @Override
    public StudentEnrollmentResponse getById(UUID id) {

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found"));

        return mapper.toResponse(enrollment);
    }

    @Override
    public List<StudentEnrollmentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public StudentEnrollmentResponse update(UUID id,
                                            StudentEnrollmentRequest request) {

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found"));

        mapper.updateEntity(enrollment, request);

        enrollment = repository.save(enrollment);

        return mapper.toResponse(enrollment);
    }

    @Override
    public void delete(UUID id) {

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found"));

        repository.delete(enrollment);
    }
}