package com.nextgen.erp.academic_management.ClassRoom.service.impl;

import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomRequest;
import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomResponse;
import com.nextgen.erp.academic_management.ClassRoom.entity.ClassRoom;
import com.nextgen.erp.academic_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.academic_management.exception.ResourceNotFoundException;
import com.nextgen.erp.academic_management.ClassRoom.mapper.ClassRoomMapper;
import com.nextgen.erp.academic_management.ClassRoom.repository.ClassRoomRepository;
import com.nextgen.erp.academic_management.ClassRoom.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassRoomServiceImpl implements ClassRoomService{

    private final ClassRoomRepository repository;
    private final ClassRoomMapper mapper;

    @Override
    public ClassRoomResponse create(ClassRoomRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Class already exists.");
        }

        ClassRoom entity = mapper.toEntity(request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ClassRoomResponse getById(UUID id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<ClassRoomResponse> getAll() {

        return repository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ClassRoomResponse update(UUID id,
                                    ClassRoomRequest request) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));

        if (!entity.getName().equals(request.getName())
                && repository.existsByName(request.getName())) {

            throw new ResourceAlreadyExistsException("Class already exists.");
        }

        entity.setName(request.getName());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setActive(request.getActive());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));

        repository.delete(entity);
    }
}
