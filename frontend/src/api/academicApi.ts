import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  AcademicYear,
  AcademicYearRequest,
  ClassRoom,
  ClassRoomRequest,
  ClassSection,
  ClassSectionRequest,
  Section,
  SectionRequest,
  Subject,
  SubjectRequest,
} from "../types/academic";

const client = createApiClient(SERVICE_URLS.academic);

export const academicYearApi = {
  list: () => client.get<AcademicYear[]>("/api/v1/academic-years"),
  get: (id: string) => client.get<AcademicYear>(`/api/v1/academic-years/${id}`),
  create: (payload: AcademicYearRequest) =>
    client.post<AcademicYear>("/api/v1/academic-years", payload),
  update: (id: string, payload: AcademicYearRequest) =>
    client.put<AcademicYear>(`/api/v1/academic-years/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/academic-years/${id}`),
  activate: (id: string) =>
    client.patch<AcademicYear>(`/api/v1/academic-years/${id}/activate`),
};

export const classRoomApi = {
  list: () => client.get<ClassRoom[]>("/api/v1/classes"),
  get: (id: string) => client.get<ClassRoom>(`/api/v1/classes/${id}`),
  create: (payload: ClassRoomRequest) =>
    client.post<ClassRoom>("/api/v1/classes", payload),
  update: (id: string, payload: ClassRoomRequest) =>
    client.put<ClassRoom>(`/api/v1/classes/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/classes/${id}`),
};

export const sectionApi = {
  list: () => client.get<Section[]>("/api/v1/sections"),
  get: (id: string) => client.get<Section>(`/api/v1/sections/${id}`),
  create: (payload: SectionRequest) =>
    client.post<Section>("/api/v1/sections", payload),
  update: (id: string, payload: SectionRequest) =>
    client.put<Section>(`/api/v1/sections/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/sections/${id}`),
};

export const subjectApi = {
  list: () => client.get<Subject[]>("/api/v1/subjects"),
  get: (id: string) => client.get<Subject>(`/api/v1/subjects/${id}`),
  create: (payload: SubjectRequest) =>
    client.post<Subject>("/api/v1/subjects", payload),
  update: (id: string, payload: SubjectRequest) =>
    client.put<Subject>(`/api/v1/subjects/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/subjects/${id}`),
};

export const classSectionApi = {
  list: () => client.get<ClassSection[]>("/api/v1/class-sections"),
  get: (id: string) => client.get<ClassSection>(`/api/v1/class-sections/${id}`),
  create: (payload: ClassSectionRequest) =>
    client.post<ClassSection>("/api/v1/class-sections", payload),
  update: (id: string, payload: ClassSectionRequest) =>
    client.put<ClassSection>(`/api/v1/class-sections/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/class-sections/${id}`),
};
