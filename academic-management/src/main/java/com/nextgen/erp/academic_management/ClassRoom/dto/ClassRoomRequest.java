package com.nextgen.erp.academic_management.ClassRoom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassRoomRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer displayOrder;

    private Boolean active;
}
