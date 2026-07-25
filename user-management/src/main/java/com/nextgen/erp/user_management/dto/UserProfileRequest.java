package com.nextgen.erp.user_management.dto;

import com.nextgen.erp.common.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Request object for creating or updating a user profile")
public class UserProfileRequest {

    @Schema(
            description = "User ID from Auth Service",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @NotNull(message = "User Id is required")
    private UUID userId;

    @Schema(example = "Putul", description = "First Name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(example = "Kumari", description = "Last Name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(example = "9876543210", description = "Mobile Number")
    private String phone;

    @Schema(
            example = "FEMALE",
            description = "Gender (MALE, FEMALE, OTHER)"
    )
    private Gender gender;

    @Schema(
            example = "1999-09-11",
            description = "Date of Birth (yyyy-MM-dd)"
    )
    private LocalDate dateOfBirth;

    @Schema(example = "Saharsa, Bihar")
    private String address;

    @Schema(example = "Saharsa")
    private String city;

    @Schema(example = "Bihar")
    private String state;

    @Schema(example = "India")
    private String country;

    @Schema(example = "true")
    private Boolean active;
}