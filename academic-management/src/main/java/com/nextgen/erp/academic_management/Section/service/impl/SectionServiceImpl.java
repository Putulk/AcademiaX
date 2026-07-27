package com.nextgen.erp.academic_management.Section.service.impl;

import com.nextgen.erp.academic_management.Section.dto.SectionRequest;
import com.nextgen.erp.academic_management.Section.dto.SectionResponse;
import com.nextgen.erp.academic_management.Section.entity.Section;
import com.nextgen.erp.academic_management.Section.mapper.SectionMapper;
import com.nextgen.erp.academic_management.Section.repository.SectionRepository;
import com.nextgen.erp.academic_management.Section.service.SectionService;
import com.nextgen.erp.academic_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.academic_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final SectionRepository repository;
    private final SectionMapper mapper;

    @Override
    public SectionResponse create(SectionRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Section already exists.");
        }

        Section entity = mapper.toEntity(request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SectionResponse getById(UUID id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Section not found"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<SectionResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public SectionResponse update(UUID id,
                                  SectionRequest request) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Section not found"));

        if (!entity.getName().equals(request.getName())
                && repository.existsByName(request.getName())) {

            throw new ResourceAlreadyExistsException("Section already exists.");
        }

        entity.setName(request.getName());
        entity.setCapacity(request.getCapacity());
        entity.setActive(request.getActive());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Section not found"));

        repository.delete(entity);
    }
}
