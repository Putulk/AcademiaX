package com.nextgen.erp.user_management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserProfileRequest {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String country;
}
