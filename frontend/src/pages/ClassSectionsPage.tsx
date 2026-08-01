import { classSectionApi } from "../api/academicApi";
import {
  loadAcademicYearOptions,
  loadClassRoomOptions,
  loadSectionOptions,
  loadTeacherOptions,
} from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { ClassSection, ClassSectionRequest } from "../types/academic";

const columns: CrudColumn<ClassSection>[] = [
  { key: "academicYearId", label: "Academic Year", lookup: loadAcademicYearOptions },
  { key: "classRoomId", label: "Class", lookup: loadClassRoomOptions },
  { key: "sectionId", label: "Section", lookup: loadSectionOptions },
  { key: "roomNumber", label: "Room" },
  { key: "capacity", label: "Capacity" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<ClassSectionRequest>[] = [
  { key: "academicYearId", label: "Academic Year", type: "reference", loadOptions: loadAcademicYearOptions, required: true },
  { key: "classRoomId", label: "Class", type: "reference", loadOptions: loadClassRoomOptions, required: true },
  { key: "sectionId", label: "Section", type: "reference", loadOptions: loadSectionOptions, required: true },
  { key: "classTeacherId", label: "Class Teacher (optional)", type: "reference", loadOptions: loadTeacherOptions },
  { key: "roomNumber", label: "Room Number", type: "text", required: true },
  { key: "capacity", label: "Capacity", type: "number", required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

export function ClassSectionsPage() {
  return (
    <CrudPage
      title="Class Sections"
      subtitle="Links an academic year + class + section into one enrollable unit."
      api={classSectionApi}
      columns={columns}
      fields={fields}
      emptyForm={{
        academicYearId: "",
        classRoomId: "",
        sectionId: "",
        classTeacherId: "",
        roomNumber: "",
        capacity: 40,
        active: true,
      }}
      toRequest={(item) => ({
        academicYearId: item.academicYearId,
        classRoomId: item.classRoomId,
        sectionId: item.sectionId,
        classTeacherId: item.classTeacherId ?? "",
        roomNumber: item.roomNumber,
        capacity: item.capacity,
        active: item.active,
      })}
      itemLabel={(item) => item.roomNumber}
    />
  );
}
