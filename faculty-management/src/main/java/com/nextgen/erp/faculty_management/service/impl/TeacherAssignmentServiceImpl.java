package com.nextgen.erp.faculty_management.service.impl;

import com.nextgen.erp.faculty_management.dto.TeacherAssignmentRequest;
import com.nextgen.erp.faculty_management.dto.TeacherAssignmentResponse;
import com.nextgen.erp.faculty_management.entity.Teacher;
import com.nextgen.erp.faculty_management.entity.TeacherAssignment;
import com.nextgen.erp.faculty_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.faculty_management.exception.ResourceNotFoundException;
import com.nextgen.erp.faculty_management.mapper.TeacherAssignmentMapper;
import com.nextgen.erp.faculty_management.repository.TeacherAssignmentRepository;
import com.nextgen.erp.faculty_management.repository.TeacherRepository;
import com.nextgen.erp.faculty_management.service.TeacherAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private final TeacherAssignmentRepository repository;
    private final TeacherRepository teacherRepository;
    private final TeacherAssignmentMapper mapper;

    @Override
    public TeacherAssignmentResponse create(TeacherAssignmentRequest request) {

        // Validate Teacher
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        // TODO
        // Validate Subject using Academic Service
        // Validate ClassSection using Academic Service
        // Validate AcademicYear using Academic Service

        boolean exists = repository
                .existsByTeacherIdAndClassSectionIdAndSubjectIdAndAcademicYearId(
                        request.getTeacherId(),
                        request.getClassSectionId(),
                        request.getSubjectId(),
                        request.getAcademicYearId());

        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Teacher is already assigned to this subject and class.");
        }

        TeacherAssignment assignment = mapper.toEntity(request);

        assignment = repository.save(assignment);

        return mapper.toResponse(assignment);
    }

    @Override
    public TeacherAssignmentResponse getById(UUID id) {

        TeacherAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher Assignment not found"));

        return mapper.toResponse(assignment);
    }

    @Override
    public List<TeacherAssignmentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TeacherAssignmentResponse update(UUID id,
                                            TeacherAssignmentRequest request) {

        TeacherAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher Assignment not found"));

        teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        mapper.updateEntity(assignment, request);

        assignment = repository.save(assignment);

        return mapper.toResponse(assignment);
    }

    @Override
    public void delete(UUID id) {

        TeacherAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher Assignment not found"));

        repository.delete(assignment);
    }
}
