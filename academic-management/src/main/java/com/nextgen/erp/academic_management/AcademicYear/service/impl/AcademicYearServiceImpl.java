package com.nextgen.erp.academic_management.AcademicYear.service.impl;

import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearRequest;
import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearResponse;
import com.nextgen.erp.academic_management.AcademicYear.entity.AcademicYear;
import com.nextgen.erp.academic_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.academic_management.exception.ResourceNotFoundException;
import com.nextgen.erp.academic_management.AcademicYear.mapper.AcademicYearMapper;
import com.nextgen.erp.academic_management.AcademicYear.repository.AcademicYearRepository;
import com.nextgen.erp.academic_management.AcademicYear.service.AcademicYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository repository;
    private final AcademicYearMapper mapper;

    @Override
    public AcademicYearResponse create(AcademicYearRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Academic Year already exists.");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
        }

        AcademicYear entity = mapper.toEntity(request);

        if (Boolean.TRUE.equals(request.getActive())) {
            repository.findByActiveTrue()
                    .ifPresent(active -> {
                        active.setActive(false);
                        repository.save(active);
                    });

            entity.setActive(true);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public AcademicYearResponse getById(UUID id) {

        AcademicYear entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Academic Year not found"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<AcademicYearResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AcademicYearResponse update(UUID id,
                                       AcademicYearRequest request) {

        AcademicYear entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Academic Year not found"));

        if (!entity.getName().equals(request.getName())
                && repository.existsByName(request.getName())) {

            throw new ResourceAlreadyExistsException(
                    "Academic Year already exists.");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
        }

        entity.setName(request.getName());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());

        if (Boolean.TRUE.equals(request.getActive())) {

            repository.findByActiveTrue()
                    .ifPresent(active -> {
                        active.setActive(false);
                        repository.save(active);
                    });

            entity.setActive(true);

        } else {

            entity.setActive(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {

        AcademicYear entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Academic Year not found"));

        repository.delete(entity);
    }

    @Override
    public AcademicYearResponse activate(UUID id) {

        AcademicYear entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Academic Year not found"));

        repository.findByActiveTrue()
                .ifPresent(active -> {
                    active.setActive(false);
                    repository.save(active);
                });

        entity.setActive(true);

        return mapper.toResponse(repository.save(entity));
    }
}