package com.nextgen.erp.academic_management.Section.mapper;

import com.nextgen.erp.academic_management.Section.dto.SectionRequest;
import com.nextgen.erp.academic_management.Section.dto.SectionResponse;
import com.nextgen.erp.academic_management.Section.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {

    public Section toEntity(SectionRequest request) {

        return Section.builder()
                .name(request.getName())
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

    public SectionResponse toResponse(Section entity) {

        return SectionResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
