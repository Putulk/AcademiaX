package com.nextgen.erp.academic_management.ClassSection.mapper;

import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionRequest;
import com.nextgen.erp.academic_management.ClassSection.dto.ClassSectionResponse;
import com.nextgen.erp.academic_management.ClassSection.entity.ClassSection;
import org.springframework.stereotype.Component;

@Component
public class ClassSectionMapper {

    public ClassSection toEntity(ClassSectionRequest request) {

        return ClassSection.builder()
                .academicYearId(request.getAcademicYearId())
                .classRoomId(request.getClassRoomId())
                .sectionId(request.getSectionId())
                .classTeacherId(request.getClassTeacherId())
                .roomNumber(request.getRoomNumber())
                .capacity(
                        request.getCapacity() == null
                                ? 40
                                : request.getCapacity()
                )
                .active(
                        request.getActive() == null
                                ? true
                                : request.getActive()
                )
                .build();
    }

    public ClassSectionResponse toResponse(ClassSection entity) {

        return ClassSectionResponse.builder()
                .id(entity.getId())
                .academicYearId(entity.getAcademicYearId())
                .classRoomId(entity.getClassRoomId())
                .sectionId(entity.getSectionId())
                .classTeacherId(entity.getClassTeacherId())
                .roomNumber(entity.getRoomNumber())
                .capacity(entity.getCapacity())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
