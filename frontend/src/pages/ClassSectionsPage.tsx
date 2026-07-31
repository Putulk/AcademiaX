import { classSectionApi } from "../api/academicApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { ClassSection, ClassSectionRequest } from "../types/academic";

const columns: CrudColumn<ClassSection>[] = [
  { key: "academicYearId", label: "Academic Year ID", mono: true },
  { key: "classRoomId", label: "Class ID", mono: true },
  { key: "sectionId", label: "Section ID", mono: true },
  { key: "roomNumber", label: "Room" },
  { key: "capacity", label: "Capacity" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<ClassSectionRequest>[] = [
  { key: "academicYearId", label: "Academic Year ID (UUID)", type: "text", required: true },
  { key: "classRoomId", label: "Class ID (UUID)", type: "text", required: true },
  { key: "sectionId", label: "Section ID (UUID)", type: "text", required: true },
  { key: "classTeacherId", label: "Class Teacher ID (UUID, optional)", type: "text" },
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
