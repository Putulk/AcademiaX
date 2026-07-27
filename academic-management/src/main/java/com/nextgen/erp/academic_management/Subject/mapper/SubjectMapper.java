package com.nextgen.erp.academic_management.Subject.mapper;

import com.nextgen.erp.academic_management.Subject.dto.SubjectRequest;
import com.nextgen.erp.academic_management.Subject.dto.SubjectResponse;
import com.nextgen.erp.academic_management.Subject.entity.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectRequest request) {

        return Subject.builder()
                .name(request.getName())
                .code(request.getCode())
                .type(request.getType())
                .active(
                        request.getActive() == null
                                ? true
                                : request.getActive()
                )
                .build();
    }

    public SubjectResponse toResponse(Subject entity) {

        return SubjectResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .type(entity.getType())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}