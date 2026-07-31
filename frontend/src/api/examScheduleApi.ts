import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type { ExamSchedule, ExamScheduleRequest } from "../types/examSchedule";

const client = createApiClient(SERVICE_URLS.examination);
const BASE = "/api/v1/exam-schedules";

export const examScheduleApi = {
  list: () => client.get<ExamSchedule[]>(BASE),
  get: (id: string) => client.get<ExamSchedule>(`${BASE}/${id}`),
  listByExam: (examId: string) =>
    client.get<ExamSchedule[]>(`${BASE}/exam/${examId}`),
  create: (payload: ExamScheduleRequest) =>
    client.post<ExamSchedule>(BASE, payload),
  update: (id: string, payload: ExamScheduleRequest) =>
    client.put<ExamSchedule>(`${BASE}/${id}`, payload),
  remove: (id: string) => client.delete<string>(`${BASE}/${id}`),
};
