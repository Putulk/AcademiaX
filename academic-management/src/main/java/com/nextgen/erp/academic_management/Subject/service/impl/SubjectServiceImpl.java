package com.nextgen.erp.academic_management.Subject.service.impl;

import com.nextgen.erp.academic_management.Subject.dto.SubjectRequest;
import com.nextgen.erp.academic_management.Subject.dto.SubjectResponse;
import com.nextgen.erp.academic_management.Subject.entity.Subject;
import com.nextgen.erp.academic_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.academic_management.exception.ResourceNotFoundException;
import com.nextgen.erp.academic_management.Subject.mapper.SubjectMapper;
import com.nextgen.erp.academic_management.Subject.repository.SubjectRepository;
import com.nextgen.erp.academic_management.Subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService{

    private final SubjectRepository repository;
    private final SubjectMapper mapper;

    @Override
    public SubjectResponse create(SubjectRequest request) {

        if (repository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("Subject code already exists.");
        }

        if (repository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Subject already exists.");
        }

        Subject entity = mapper.toEntity(request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SubjectResponse getById(UUID id) {

        Subject entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject not found"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<SubjectResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public SubjectResponse update(UUID id,
                                  SubjectRequest request) {

        Subject entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject not found"));

        if (!entity.getCode().equals(request.getCode())
                && repository.existsByCode(request.getCode())) {

            throw new ResourceAlreadyExistsException("Subject code already exists.");
        }

        if (!entity.getName().equals(request.getName())
                && repository.existsByName(request.getName())) {

            throw new ResourceAlreadyExistsException("Subject already exists.");
        }

        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setType(request.getType());
        entity.setActive(request.getActive());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {

        Subject entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject not found"));

        repository.delete(entity);
    }

    @Override
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }
}
