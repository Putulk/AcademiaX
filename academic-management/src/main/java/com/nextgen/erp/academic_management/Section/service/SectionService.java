package com.nextgen.erp.academic_management.Section.service;

import com.nextgen.erp.academic_management.Section.dto.SectionRequest;
import com.nextgen.erp.academic_management.Section.dto.SectionResponse;

import java.util.List;
import java.util.UUID;

public interface SectionService {

    SectionResponse create(SectionRequest request);

    SectionResponse getById(UUID id);

    List<SectionResponse> getAll();

    SectionResponse update(UUID id, SectionRequest request);

    void delete(UUID id);

    boolean exists(UUID id);
}
