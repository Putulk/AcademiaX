package com.nextgen.erp.academic_management.ClassSection.entity;

import com.nextgen.erp.academic_management.AcademicYear.entity.AcademicYear;
import com.nextgen.erp.academic_management.ClassRoom.entity.ClassRoom;
import com.nextgen.erp.academic_management.Section.entity.Section;
import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "class_sections",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "academic_year_id",
                        "class_room_id",
                        "section_id"
                })
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSection extends BaseEntity {

    @Column(name = "academic_year_id", nullable = false)
    private UUID academicYearId;

    @Column(name = "class_room_id", nullable = false)
    private UUID classRoomId;

    @Column(name = "section_id", nullable = false)
    private UUID sectionId;

    private UUID classTeacherId;

    private String roomNumber;

    @Builder.Default
    private Integer capacity = 40;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year")
    private AcademicYear academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room")
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section")
    private Section section;

    @Builder.Default
    private Boolean active = true;
}