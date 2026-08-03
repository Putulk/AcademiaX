export type DataType = "TEXT" | "NUMBER" | "DATE" | "BOOLEAN" | "ENUM" | "REFERENCE";

export const DATA_TYPES: DataType[] = ["TEXT", "NUMBER", "DATE", "BOOLEAN", "ENUM", "REFERENCE"];

export interface EntityDefinition {
  id: string;
  tenantId: string;
  name: string;
  pluralLabel: string;
  description?: string;
  displayFieldName: string;
  active: boolean;
}

export interface EntityDefinitionRequest {
  name: string;
  pluralLabel: string;
  description?: string;
  displayFieldName: string;
  active: boolean;
}

export interface FieldDefinition {
  id: string;
  entityDefinitionId: string;
  name: string;
  label: string;
  dataType: DataType;
  required: boolean;
  referenceTargetEntityDefinitionId?: string;
  enumOptions?: string[];
  displayOrder: number;
}

export interface FieldDefinitionRequest {
  name: string;
  label: string;
  dataType: DataType;
  required: boolean;
  referenceTargetEntityDefinitionId?: string;
  enumOptions?: string[];
  displayOrder?: number;
}

export interface EntityRecord {
  id: string;
  entityDefinitionId: string;
  data: Record<string, unknown>;
  createdAt: string;
  updatedAt: string;
}

export interface EntityRecordRequest {
  data: Record<string, unknown>;
}
