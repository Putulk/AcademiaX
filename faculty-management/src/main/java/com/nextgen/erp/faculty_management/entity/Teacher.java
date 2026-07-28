package com.nextgen.erp.faculty_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "teachers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "employee_code"),
                @UniqueConstraint(columnNames = "user_profile_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity{

    @Column(name = "user_profile_id", nullable = false)
    private UUID userProfileId;

    @Column(name = "employee_code", nullable = false, length = 30)
    private String employeeCode;

    @Column(length = 100)
    private String designation;

    @Column(length = 100)
    private String department;

    @Column(nullable = false)
    private LocalDate joiningDate;

    private Integer experienceYears;

    private BigDecimal salary;

    @Builder.Default
    private Boolean active = true;
}
