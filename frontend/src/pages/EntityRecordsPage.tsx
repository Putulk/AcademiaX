import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { entityDefinitionApi, entityRecordApi } from "../api/platformApi";
import { ApiError } from "../api/client";
import { CrudPage, type CrudApi, type CrudField, type CrudColumn } from "../components/CrudPage";
import type { ReferenceOption } from "../api/directory";
import { useToasts } from "../hooks/useToasts";
import type { EntityDefinition, FieldDefinition } from "../types/platform";

type RecordRow = { id: string } & Record<string, unknown>;
type RecordForm = Record<string, unknown>;

function defaultValueFor(field: FieldDefinition): unknown {
  switch (field.dataType) {
    case "NUMBER":
      return 0;
    case "BOOLEAN":
      return false;
    default:
      return "";
  }
}

function buildReferenceLoader(field: FieldDefinition): () => Promise<ReferenceOption[]> {
  return async () => {
    if (!field.referenceTargetEntityDefinitionId) return [];

    const [targetDefinition, records] = await Promise.all([
      entityDefinitionApi.get(field.referenceTargetEntityDefinitionId),
      entityRecordApi.list(field.referenceTargetEntityDefinitionId),
    ]);

    return records.map((r) => ({
      value: r.id,
      label: String(r.data[targetDefinition.displayFieldName] ?? r.id),
    }));
  };
}

export function EntityRecordsPage() {
  const { entityDefinitionId } = useParams<{ entityDefinitionId: string }>();
  const [definition, setDefinition] = useState<EntityDefinition | null>(null);
  const [fieldDefs, setFieldDefs] = useState<FieldDefinition[]>([]);
  const [loading, setLoading] = useState(true);
  const toasts = useToasts();

  useEffect(() => {
    if (!entityDefinitionId) return;

    setLoading(true);
    Promise.all([
      entityDefinitionApi.get(entityDefinitionId),
      entityDefinitionApi.listFields(entityDefinitionId),
    ])
      .then(([def, flds]) => {
        setDefinition(def);
        setFieldDefs(flds.slice().sort((a, b) => a.displayOrder - b.displayOrder));
      })
      .catch((err) =>
        toasts.error(err instanceof ApiError ? err.message : "Failed to load entity"),
      )
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [entityDefinitionId]);

  if (!entityDefinitionId) return null;

  if (loading || !definition) {
    return <div className="empty-state">Loading…</div>;
  }

  const columns: CrudColumn<RecordRow>[] = fieldDefs.map((field) => ({
    key: field.name,
    label: field.label,
    mono: field.dataType === "REFERENCE",
    lookup: field.dataType === "REFERENCE" ? buildReferenceLoader(field) : undefined,
    render:
      field.dataType === "BOOLEAN"
        ? (item) => (item[field.name] ? "Yes" : "No")
        : undefined,
  }));

  const formFields: CrudField<RecordForm>[] = fieldDefs.map((field) => {
    if (field.dataType === "REFERENCE") {
      return {
        key: field.name,
        label: field.label,
        type: "reference",
        required: field.required,
        loadOptions: buildReferenceLoader(field),
      };
    }
    if (field.dataType === "ENUM") {
      return {
        key: field.name,
        label: field.label,
        type: "select",
        required: field.required,
        options: field.enumOptions ?? [],
      };
    }
    if (field.dataType === "BOOLEAN") {
      return { key: field.name, label: field.label, type: "checkbox" };
    }
    if (field.dataType === "NUMBER") {
      return { key: field.name, label: field.label, type: "number", required: field.required };
    }
    if (field.dataType === "DATE") {
      return { key: field.name, label: field.label, type: "date", required: field.required };
    }
    return { key: field.name, label: field.label, type: "text", required: field.required };
  });

  const emptyForm: RecordForm = {};
  fieldDefs.forEach((field) => {
    emptyForm[field.name] = defaultValueFor(field);
  });

  const api: CrudApi<RecordRow, RecordForm> = {
    list: async () =>
      (await entityRecordApi.list(entityDefinitionId)).map((r) => ({ id: r.id, ...r.data })),
    create: async (payload) => {
      const r = await entityRecordApi.create(entityDefinitionId, { data: payload });
      return { id: r.id, ...r.data };
    },
    update: async (id, payload) => {
      const r = await entityRecordApi.update(entityDefinitionId, id, { data: payload });
      return { id: r.id, ...r.data };
    },
    remove: (id) => entityRecordApi.remove(entityDefinitionId, id),
  };

  const toRequest = (item: RecordRow): RecordForm => {
    const { id: _id, ...rest } = item;
    return rest;
  };

  return (
    <CrudPage
      key={entityDefinitionId}
      title={definition.pluralLabel}
      subtitle={
        definition.description ||
        `Records of "${definition.name}" — a custom entity type, no page was written for this.`
      }
      api={api}
      columns={columns}
      fields={formFields}
      emptyForm={emptyForm}
      toRequest={toRequest}
      itemLabel={(item) => String(item[definition.displayFieldName] ?? item.id)}
    />
  );
}
