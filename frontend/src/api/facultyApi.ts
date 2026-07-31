import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  Teacher,
  TeacherRequest,
  TeacherAssignment,
  TeacherAssignmentRequest,
} from "../types/faculty";

const client = createApiClient(SERVICE_URLS.faculty);

export const teacherApi = {
  list: () => client.get<Teacher[]>("/api/v1/teachers"),
  get: (id: string) => client.get<Teacher>(`/api/v1/teachers/${id}`),
  create: (payload: TeacherRequest) =>
    client.post<Teacher>("/api/v1/teachers", payload),
  update: (id: string, payload: TeacherRequest) =>
    client.put<Teacher>(`/api/v1/teachers/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/teachers/${id}`),
};

export const teacherAssignmentApi = {
  list: () => client.get<TeacherAssignment[]>("/api/v1/teacher-assignments"),
  get: (id: string) =>
    client.get<TeacherAssignment>(`/api/v1/teacher-assignments/${id}`),
  create: (payload: TeacherAssignmentRequest) =>
    client.post<TeacherAssignment>("/api/v1/teacher-assignments", payload),
  update: (id: string, payload: TeacherAssignmentRequest) =>
    client.put<TeacherAssignment>(`/api/v1/teacher-assignments/${id}`, payload),
  remove: (id: string) =>
    client.delete<void>(`/api/v1/teacher-assignments/${id}`),
};
