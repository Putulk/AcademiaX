package com.nextgen.erp.academic_management.ClassRoom.service;

import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomRequest;
import com.nextgen.erp.academic_management.ClassRoom.dto.ClassRoomResponse;

import java.util.List;
import java.util.UUID;

public interface ClassRoomService {

    ClassRoomResponse create(ClassRoomRequest request);

    ClassRoomResponse getById(UUID id);

    List<ClassRoomResponse> getAll();

    ClassRoomResponse update(UUID id, ClassRoomRequest request);

    void delete(UUID id);
}
