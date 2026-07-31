import { classRoomApi } from "../api/academicApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { ClassRoom, ClassRoomRequest } from "../types/academic";

const columns: CrudColumn<ClassRoom>[] = [
  { key: "name", label: "Name" },
  { key: "displayOrder", label: "Display Order" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<ClassRoomRequest>[] = [
  { key: "name", label: "Name", type: "text", required: true, placeholder: "Grade 10" },
  { key: "displayOrder", label: "Display Order", type: "number", required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

export function ClassRoomsPage() {
  return (
    <CrudPage
      title="Classes"
      subtitle="Grade/class levels (e.g. Grade 10) used across the school."
      api={classRoomApi}
      columns={columns}
      fields={fields}
      emptyForm={{ name: "", displayOrder: 0, active: true }}
      toRequest={(item) => ({
        name: item.name,
        displayOrder: item.displayOrder,
        active: item.active,
      })}
      itemLabel={(item) => item.name}
    />
  );
}
