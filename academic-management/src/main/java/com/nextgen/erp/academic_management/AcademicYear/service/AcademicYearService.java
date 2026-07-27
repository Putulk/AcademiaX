package com.nextgen.erp.academic_management.AcademicYear.service;

import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearRequest;
import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearResponse;

import java.util.List;
import java.util.UUID;

public interface AcademicYearService {

    AcademicYearResponse create(AcademicYearRequest request);

    AcademicYearResponse getById(UUID id);

    List<AcademicYearResponse> getAll();

    AcademicYearResponse update(UUID id,
                                AcademicYearRequest request);

    void delete(UUID id);

    AcademicYearResponse activate(UUID id);
}
