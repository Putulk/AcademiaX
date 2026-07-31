import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type { ExamResult, ExamResultRequest } from "../types/examResult";

const client = createApiClient(SERVICE_URLS.examination);
const BASE = "/api/v1/exam-results";

export const examResultApi = {
  list: () => client.get<ExamResult[]>(BASE),
  get: (id: string) => client.get<ExamResult>(`${BASE}/${id}`),
  listByExam: (examId: string) =>
    client.get<ExamResult[]>(`${BASE}/exam/${examId}`),
  resultCard: (examId: string, studentEnrollmentId: string) =>
    client.get<ExamResult[]>(
      `${BASE}/exam/${examId}/student-enrollment/${studentEnrollmentId}`,
    ),
  create: (payload: ExamResultRequest) => client.post<ExamResult>(BASE, payload),
  update: (id: string, payload: ExamResultRequest) =>
    client.put<ExamResult>(`${BASE}/${id}`, payload),
  remove: (id: string) => client.delete<string>(`${BASE}/${id}`),
};
