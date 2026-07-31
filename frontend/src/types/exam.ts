export type ExamStatus =
  | "DRAFT"
  | "SCHEDULED"
  | "ONGOING"
  | "COMPLETED"
  | "PUBLISHED"
  | "CANCELLED";

export const EXAM_STATUSES: ExamStatus[] = [
  "DRAFT",
  "SCHEDULED",
  "ONGOING",
  "COMPLETED",
  "PUBLISHED",
  "CANCELLED",
];

export interface Exam {
  id: string;
  name: string;
  academicYear: string;
  classId: string;
  startDate: string;
  endDate: string;
  status: ExamStatus;
  description?: string;
}

export interface ExamRequest {
  name: string;
  academicYear: string;
  classId: string;
  startDate: string;
  endDate: string;
  status: ExamStatus;
  description?: string;
}
