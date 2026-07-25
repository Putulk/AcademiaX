package com.nextgen.erp.user_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userProfileId;

    @Column(nullable = false, unique = true)
    private String admissionNumber;

    @Column(nullable = false)
    private String rollNumber;

    private String academicYear;

    private LocalDate admissionDate;

    private String bloodGroup;

    private String religion;

    private String category;

    private String house;

    @Builder.Default
    private Boolean active = true;
}
