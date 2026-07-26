package com.nextgen.erp.student_management.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfileResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private Boolean active;
}
