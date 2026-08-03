import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  EntityDefinition,
  EntityDefinitionRequest,
  EntityRecord,
  EntityRecordRequest,
  FieldDefinition,
  FieldDefinitionRequest,
} from "../types/platform";

// Real multi-tenancy (tenant onboarding, JWT tenant claim) is Phase 2 — for
// now every request in this browser is scoped to one fixed demo tenant so
// the generic entity engine has something to isolate by.
export const DEMO_TENANT_ID = "00000000-0000-0000-0000-000000000001";

const client = createApiClient(SERVICE_URLS.platform, { "X-Tenant-Id": DEMO_TENANT_ID });

const DEFINITIONS_BASE = "/api/v1/platform/entity-definitions";

export const entityDefinitionApi = {
  list: () => client.get<EntityDefinition[]>(DEFINITIONS_BASE),
  get: (id: string) => client.get<EntityDefinition>(`${DEFINITIONS_BASE}/${id}`),
  create: (payload: EntityDefinitionRequest) =>
    client.post<EntityDefinition>(DEFINITIONS_BASE, payload),
  update: (id: string, payload: EntityDefinitionRequest) =>
    client.put<EntityDefinition>(`${DEFINITIONS_BASE}/${id}`, payload),
  remove: (id: string) => client.delete<string>(`${DEFINITIONS_BASE}/${id}`),

  listFields: (entityDefinitionId: string) =>
    client.get<FieldDefinition[]>(`${DEFINITIONS_BASE}/${entityDefinitionId}/fields`),
  addField: (entityDefinitionId: string, payload: FieldDefinitionRequest) =>
    client.post<FieldDefinition>(`${DEFINITIONS_BASE}/${entityDefinitionId}/fields`, payload),
  updateField: (entityDefinitionId: string, fieldId: string, payload: FieldDefinitionRequest) =>
    client.put<FieldDefinition>(
      `${DEFINITIONS_BASE}/${entityDefinitionId}/fields/${fieldId}`,
      payload,
    ),
  removeField: (entityDefinitionId: string, fieldId: string) =>
    client.delete<string>(`${DEFINITIONS_BASE}/${entityDefinitionId}/fields/${fieldId}`),
};

const recordsBase = (entityDefinitionId: string) =>
  `/api/v1/platform/entities/${entityDefinitionId}/records`;

export const entityRecordApi = {
  list: (entityDefinitionId: string) =>
    client.get<EntityRecord[]>(recordsBase(entityDefinitionId)),
  get: (entityDefinitionId: string, id: string) =>
    client.get<EntityRecord>(`${recordsBase(entityDefinitionId)}/${id}`),
  create: (entityDefinitionId: string, payload: EntityRecordRequest) =>
    client.post<EntityRecord>(recordsBase(entityDefinitionId), payload),
  update: (entityDefinitionId: string, id: string, payload: EntityRecordRequest) =>
    client.put<EntityRecord>(`${recordsBase(entityDefinitionId)}/${id}`, payload),
  remove: (entityDefinitionId: string, id: string) =>
    client.delete<string>(`${recordsBase(entityDefinitionId)}/${id}`),
  label: (entityDefinitionId: string, id: string) =>
    client.get<string>(`${recordsBase(entityDefinitionId)}/${id}/label`),
};
