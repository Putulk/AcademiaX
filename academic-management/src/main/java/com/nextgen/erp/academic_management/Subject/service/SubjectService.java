package com.nextgen.erp.academic_management.Subject.service;

import com.nextgen.erp.academic_management.Subject.dto.SubjectRequest;
import com.nextgen.erp.academic_management.Subject.dto.SubjectResponse;

import java.util.List;
import java.util.UUID;

public interface SubjectService {

    SubjectResponse create(SubjectRequest request);

    SubjectResponse getById(UUID id);

    List<SubjectResponse> getAll();

    SubjectResponse update(UUID id, SubjectRequest request);

    void delete(UUID id);
    boolean exists(UUID id);
}