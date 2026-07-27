package com.nextgen.erp.student_management.service.impl;

import com.nextgen.erp.student_management.dto.StudentRequest;
import com.nextgen.erp.student_management.dto.StudentResponse;
import com.nextgen.erp.student_management.entity.Student;
import com.nextgen.erp.student_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.student_management.exception.ResourceNotFoundException;
import com.nextgen.erp.student_management.mapper.StudentMapper;
import com.nextgen.erp.student_management.repository.StudentRepository;
import com.nextgen.erp.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    public StudentResponse create(StudentRequest request) {

        if (repository.existsByAdmissionNumber(request.getAdmissionNumber())) {
            throw new ResourceAlreadyExistsException(
                    "Admission number already exists.");
        }

        if (repository.existsByUserProfileId(request.getUserProfileId())) {
            throw new ResourceAlreadyExistsException(
                    "Student already exists for this user profile.");
        }

        if (request.getClassId() != null &&
                repository.existsByClassIdAndRollNumber(
                        request.getClassId(),
                        request.getRollNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Roll number already exists in this class.");
        }

        Student student = mapper.toEntity(request);

        return mapper.toResponse(repository.save(student));
    }

    @Override
    public StudentResponse getById(UUID id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        return mapper.toResponse(student);
    }

    @Override
    public List<StudentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public StudentResponse update(UUID id,
                                  StudentRequest request) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        student.setClassId(request.getClassId());
        student.setSectionId(request.getSectionId());
        student.setAcademicYear(request.getAcademicYear());
        student.setAdmissionDate(request.getAdmissionDate());
        student.setBloodGroup(request.getBloodGroup());
        student.setReligion(request.getReligion());
        student.setCategory(request.getCategory());
        student.setHouse(request.getHouse());

        if (!student.getRollNumber().equals(request.getRollNumber())) {

            if (repository.existsByClassIdAndRollNumber(
                    request.getClassId(),
                    request.getRollNumber())) {

                throw new ResourceAlreadyExistsException(
                        "Roll number already exists.");
            }

            student.setRollNumber(request.getRollNumber());
        }

        return mapper.toResponse(repository.save(student));
    }

    @Override
    public void delete(UUID id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        repository.delete(student);
    }
}