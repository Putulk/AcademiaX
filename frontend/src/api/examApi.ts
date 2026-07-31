import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type { Exam, ExamRequest } from "../types/exam";

const client = createApiClient(SERVICE_URLS.examination);
const BASE = "/api/v1/exams";

export const examApi = {
  list: () => client.get<Exam[]>(BASE),
  get: (id: string) => client.get<Exam>(`${BASE}/${id}`),
  create: (payload: ExamRequest) => client.post<Exam>(BASE, payload),
  update: (id: string, payload: ExamRequest) =>
    client.put<Exam>(`${BASE}/${id}`, payload),
  remove: (id: string) => client.delete<string>(`${BASE}/${id}`),
};
