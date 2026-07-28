package com.nextgen.erp.faculty_management.service;

import com.nextgen.erp.faculty_management.dto.TeacherAssignmentRequest;
import com.nextgen.erp.faculty_management.dto.TeacherAssignmentResponse;

import java.util.List;
import java.util.UUID;

public interface TeacherAssignmentService {

    TeacherAssignmentResponse create(TeacherAssignmentRequest request);

    TeacherAssignmentResponse getById(UUID id);

    List<TeacherAssignmentResponse> getAll();

    TeacherAssignmentResponse update(UUID id,
                                     TeacherAssignmentRequest request);

    void delete(UUID id);
}
