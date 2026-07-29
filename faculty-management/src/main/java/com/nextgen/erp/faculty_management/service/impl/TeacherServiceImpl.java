package com.nextgen.erp.faculty_management.service.impl;

import com.nextgen.erp.faculty_management.dto.TeacherRequest;
import com.nextgen.erp.faculty_management.dto.TeacherResponse;
import com.nextgen.erp.faculty_management.entity.Teacher;
import com.nextgen.erp.faculty_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.faculty_management.exception.ResourceNotFoundException;
import com.nextgen.erp.faculty_management.mapper.TeacherMapper;
import com.nextgen.erp.faculty_management.repository.TeacherRepository;
import com.nextgen.erp.faculty_management.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final TeacherMapper mapper;

    @Override
    public TeacherResponse create(TeacherRequest request) {

        if (repository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ResourceAlreadyExistsException("Employee Code already exists.");
        }

        if (repository.existsByUserProfileId(request.getUserProfileId())) {
            throw new ResourceAlreadyExistsException("User Profile is already assigned.");
        }

        Teacher teacher = mapper.toEntity(request);

        teacher = repository.save(teacher);

        return mapper.toResponse(teacher);
    }

    @Override
    public TeacherResponse getById(UUID id) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found."));

        return mapper.toResponse(teacher);
    }

    @Override
    public List<TeacherResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TeacherResponse update(UUID id,
                                  TeacherRequest request) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found."));

        mapper.updateEntity(teacher, request);

        teacher = repository.save(teacher);

        return mapper.toResponse(teacher);
    }

    @Override
    public void delete(UUID id) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found."));

        repository.delete(teacher);
    }

    @Override
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }
}