import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type { Attendance, AttendanceRequest } from "../types/attendance";

const client = createApiClient(SERVICE_URLS.attendance);
const BASE = "/api/v1/attendance";

export const attendanceApi = {
  list: () => client.get<Attendance[]>(BASE),
  get: (id: string) => client.get<Attendance>(`${BASE}/${id}`),
  listByStudent: (studentEnrollmentId: string) =>
    client.get<Attendance[]>(`${BASE}/student/${studentEnrollmentId}`),
  listByClass: (classId: string) =>
    client.get<Attendance[]>(`${BASE}/class/${classId}`),
  listByDate: (date: string) => client.get<Attendance[]>(`${BASE}/date/${date}`),
  create: (payload: AttendanceRequest) =>
    client.post<Attendance>(BASE, payload),
  update: (id: string, payload: AttendanceRequest) =>
    client.put<Attendance>(`${BASE}/${id}`, payload),
  remove: (id: string) => client.delete<string>(`${BASE}/${id}`),
};
