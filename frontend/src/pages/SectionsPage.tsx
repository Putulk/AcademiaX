import { sectionApi } from "../api/academicApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { Section, SectionRequest } from "../types/academic";

const columns: CrudColumn<Section>[] = [
  { key: "name", label: "Name" },
  { key: "capacity", label: "Capacity" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<SectionRequest>[] = [
  { key: "name", label: "Name", type: "text", required: true, placeholder: "Section A" },
  { key: "capacity", label: "Capacity", type: "number", required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

export function SectionsPage() {
  return (
    <CrudPage
      title="Sections"
      subtitle="Sections (A, B, C…) that a class is split into."
      api={sectionApi}
      columns={columns}
      fields={fields}
      emptyForm={{ name: "", capacity: 40, active: true }}
      toRequest={(item) => ({
        name: item.name,
        capacity: item.capacity,
        active: item.active,
      })}
      itemLabel={(item) => item.name}
    />
  );
}
