package com.nextgen.erp.user_management.service.impl;

import com.nextgen.erp.user_management.dto.StudentRequest;
import com.nextgen.erp.user_management.dto.StudentResponse;
import com.nextgen.erp.user_management.entity.Student;
import com.nextgen.erp.user_management.exception.ResourceAlreadyExistsException;
import com.nextgen.erp.user_management.exception.ResourceNotFoundException;
import com.nextgen.erp.user_management.repository.StudentRepository;
import com.nextgen.erp.user_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository repository;

    @Override
    public StudentResponse create(StudentRequest request) {

        if (repository.existsByAdmissionNumber(request.getAdmissionNumber())) {
            throw new ResourceAlreadyExistsException("Admission number already exists.");
        }

        if (repository.existsByUserProfileId(request.getUserProfileId())) {
            throw new ResourceAlreadyExistsException("User profile is already assigned to a student.");
        }

        if (repository.existsByRollNumber(request.getRollNumber())) {
            throw new ResourceAlreadyExistsException("Roll number already exists.");
        }

        Student student = Student.builder()
                .userProfileId(request.getUserProfileId())
                .admissionNumber(request.getAdmissionNumber())
                .rollNumber(request.getRollNumber())
                .academicYear(request.getAcademicYear())
                .admissionDate(request.getAdmissionDate())
                .bloodGroup(request.getBloodGroup())
                .religion(request.getReligion())
                .category(request.getCategory())
                .house(request.getHouse())
                .active(request.getActive())
                .build();

        repository.save(student);

        return map(student);
    }

    @Override
    public StudentResponse getById(UUID id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found."));

        return map(student);
    }

    @Override
    public List<StudentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public StudentResponse update(UUID id, StudentRequest request) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found."));

        if (!student.getAdmissionNumber().equals(request.getAdmissionNumber())
                && repository.existsByAdmissionNumber(request.getAdmissionNumber())) {
            throw new ResourceAlreadyExistsException("Admission number already exists.");
        }

        if (!student.getRollNumber().equals(request.getRollNumber())
                && repository.existsByRollNumber(request.getRollNumber())) {
            throw new ResourceAlreadyExistsException("Roll number already exists.");
        }

        student.setAdmissionNumber(request.getAdmissionNumber());
        student.setRollNumber(request.getRollNumber());
        student.setAcademicYear(request.getAcademicYear());
        student.setAdmissionDate(request.getAdmissionDate());
        student.setBloodGroup(request.getBloodGroup());
        student.setReligion(request.getReligion());
        student.setCategory(request.getCategory());
        student.setHouse(request.getHouse());
        student.setActive(request.getActive());

        repository.save(student);

        return map(student);
    }

    @Override
    public void delete(UUID id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found."));

        repository.delete(student);
    }

    private StudentResponse map(Student student) {

        return StudentResponse.builder()
                .id(student.getId())
                .userProfileId(student.getUserProfileId())
                .admissionNumber(student.getAdmissionNumber())
                .rollNumber(student.getRollNumber())
                .academicYear(student.getAcademicYear())
                .admissionDate(student.getAdmissionDate())
                .bloodGroup(student.getBloodGroup())
                .religion(student.getReligion())
                .category(student.getCategory())
                .house(student.getHouse())
                .active(student.getActive())
                .build();
    }
}
