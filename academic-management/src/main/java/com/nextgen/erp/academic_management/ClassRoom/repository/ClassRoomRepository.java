package com.nextgen.erp.academic_management.ClassRoom.repository;

import com.nextgen.erp.academic_management.ClassRoom.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRoomRepository
        extends JpaRepository<ClassRoom, UUID>{

    boolean existsByName(String name);

    List<ClassRoom> findAllByOrderByDisplayOrderAsc();
}
