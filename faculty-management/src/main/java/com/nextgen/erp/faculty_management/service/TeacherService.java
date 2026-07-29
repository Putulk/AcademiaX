package com.nextgen.erp.faculty_management.service;

import com.nextgen.erp.faculty_management.dto.TeacherRequest;
import com.nextgen.erp.faculty_management.dto.TeacherResponse;

import java.util.List;
import java.util.UUID;

public interface TeacherService {

    TeacherResponse create(TeacherRequest request);

    TeacherResponse getById(UUID id);

    List<TeacherResponse> getAll();

    TeacherResponse update(UUID id, TeacherRequest request);

    void delete(UUID id);

    boolean exists(UUID id);
}