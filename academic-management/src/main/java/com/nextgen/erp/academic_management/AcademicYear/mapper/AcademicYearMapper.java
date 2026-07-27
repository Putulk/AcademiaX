package com.nextgen.erp.academic_management.AcademicYear.mapper;

import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearRequest;
import com.nextgen.erp.academic_management.AcademicYear.dto.AcademicYearResponse;
import com.nextgen.erp.academic_management.AcademicYear.entity.AcademicYear;
import org.springframework.stereotype.Component;

@Component
public class AcademicYearMapper {

    public AcademicYear toEntity(AcademicYearRequest request) {

        return AcademicYear.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(Boolean.TRUE.equals(request.getActive()))
                .build();
    }

    public AcademicYearResponse toResponse(AcademicYear entity) {

        return AcademicYearResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}