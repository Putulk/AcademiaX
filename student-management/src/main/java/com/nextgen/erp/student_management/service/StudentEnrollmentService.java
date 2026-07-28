package com.nextgen.erp.student_management.service;

import com.nextgen.erp.student_management.dto.StudentEnrollmentRequest;
import com.nextgen.erp.student_management.dto.StudentEnrollmentResponse;

import java.util.List;
import java.util.UUID;

public interface StudentEnrollmentService {

    StudentEnrollmentResponse create(StudentEnrollmentRequest request);

    StudentEnrollmentResponse getById(UUID id);

    List<StudentEnrollmentResponse> getAll();

    StudentEnrollmentResponse update(UUID id,
                                     StudentEnrollmentRequest request);

    void delete(UUID id);
}