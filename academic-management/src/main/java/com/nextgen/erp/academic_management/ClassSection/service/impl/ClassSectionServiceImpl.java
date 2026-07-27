package com.nextgen.erp.academic_management.ClassSection.service.impl;

import com.nextgen.erp.academic_management.AcademicYear.entity.AcademicYear;
import com.nextgen.erp.academic_management.AcademicYear.repository.AcademicYearRepository;
import com.nextgen.erp.academic_management.ClassRoom.entity.ClassRoom;
import com.nextgen.erp.academic_management.ClassRoom.repository.ClassRoomRepository;
import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionRequest;
import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionResponse;
import com.nextgen.erp.academic_management.ClassSection.entity.ClassSection;
import com.nextgen.erp.academic_management.ClassSection.mapper.ClassSectionMapper;
import com.nextgen.erp.academic_management.ClassSection.repository.ClassSectionRepository;
import com.nextgen.erp.academic_management.ClassSection.service.ClassSectionService;
import com.nextgen.erp.academic_management.Section.entity.Section;
import com.nextgen.erp.academic_management.Section.repository.SectionRepository;
import com.nextgen.erp.academic_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.academic_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassSectionServiceImpl implements ClassSectionService{

    private final ClassSectionRepository repository;
    private final AcademicYearRepository academicYearRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
    private final ClassSectionMapper mapper;

    @Override
    public ClassSectionResponse create(ClassSectionRequest request) {

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic Year not found"));

        ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (repository.existsByAcademicYearIdAndClassRoomIdAndSectionId(
                academicYear.getId(),
                classRoom.getId(),
                section.getId())) {

            throw new ResourceAlreadyExistsException("Class Section already exists.");
        }

        ClassSection entity = mapper.toEntity(request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ClassSectionResponse getById(UUID id) {

        ClassSection entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class Section not found"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<ClassSectionResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ClassSectionResponse update(UUID id,
                                       ClassSectionRequest request) {

        ClassSection entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class Section not found"));

        academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic Year not found"));

        classRoomRepository.findById(request.getClassRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        entity.setAcademicYearId(request.getAcademicYearId());
        entity.setClassRoomId(request.getClassRoomId());
        entity.setSectionId(request.getSectionId());
        entity.setClassTeacherId(request.getClassTeacherId());
        entity.setRoomNumber(request.getRoomNumber());
        entity.setCapacity(request.getCapacity());
        entity.setActive(request.getActive());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {

        ClassSection entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class Section not found"));

        repository.delete(entity);
    }
}