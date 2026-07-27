package com.nextgen.erp.academic_management.ClassRoom.mapper;

import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomRequest;
import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomResponse;
import com.nextgen.erp.academic_management.ClassRoom.entity.ClassRoom;
import org.springframework.stereotype.Component;

@Component
public class ClassRoomMapper {

    public ClassRoom toEntity(ClassRoomRequest request) {

        return ClassRoom.builder()
                .name(request.getName())
                .displayOrder(request.getDisplayOrder())
                .active(
                        request.getActive() == null
                                ? true
                                : request.getActive()
                )
                .build();
    }

    public ClassRoomResponse toResponse(ClassRoom entity) {

        return ClassRoomResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .displayOrder(entity.getDisplayOrder())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}