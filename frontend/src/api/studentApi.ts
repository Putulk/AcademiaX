import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  Student,
  StudentRequest,
  StudentEnrollment,
  StudentEnrollmentRequest,
} from "../types/student";

const client = createApiClient(SERVICE_URLS.student);

export const studentApi = {
  list: () => client.get<Student[]>("/api/v1/students"),
  get: (id: string) => client.get<Student>(`/api/v1/students/${id}`),
  create: (payload: StudentRequest) =>
    client.post<Student>("/api/v1/students", payload),
  update: (id: string, payload: StudentRequest) =>
    client.put<Student>(`/api/v1/students/${id}`, payload),
  remove: (id: string) => client.delete<void>(`/api/v1/students/${id}`),
};

export const studentEnrollmentApi = {
  list: () => client.get<StudentEnrollment[]>("/api/v1/student-enrollments"),
  get: (id: string) =>
    client.get<StudentEnrollment>(`/api/v1/student-enrollments/${id}`),
  create: (payload: StudentEnrollmentRequest) =>
    client.post<StudentEnrollment>("/api/v1/student-enrollments", payload),
  update: (id: string, payload: StudentEnrollmentRequest) =>
    client.put<StudentEnrollment>(`/api/v1/student-enrollments/${id}`, payload),
  remove: (id: string) =>
    client.delete<void>(`/api/v1/student-enrollments/${id}`),
};
