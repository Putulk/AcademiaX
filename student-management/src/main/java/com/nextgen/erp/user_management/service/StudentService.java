package com.nextgen.erp.user_management.service;

import com.nextgen.erp.user_management.dto.StudentRequest;
import com.nextgen.erp.user_management.dto.StudentResponse;

import java.util.List;
import java.util.UUID;

public interface StudentService{
    StudentResponse create(StudentRequest request);

    StudentResponse getById(UUID id);

    List<StudentResponse> getAll();

    StudentResponse update(UUID id,StudentRequest request);

    void delete(UUID id);
}
