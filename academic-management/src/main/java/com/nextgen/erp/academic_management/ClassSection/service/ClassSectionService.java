package com.nextgen.erp.academic_management.ClassSection.service;


import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionRequest;
import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionResponse;

import java.util.List;
import java.util.UUID;

public interface ClassSectionService {

    ClassSectionResponse create(ClassSectionRequest request);

    ClassSectionResponse getById(UUID id);

    List<ClassSectionResponse> getAll();

    ClassSectionResponse update(UUID id,
                                ClassSectionRequest request);

    void delete(UUID id);
}
