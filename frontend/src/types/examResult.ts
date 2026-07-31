export type Grade = "A_PLUS" | "A" | "B_PLUS" | "B" | "C" | "D" | "F";

export type ResultStatus = "PASS" | "FAIL";

export interface ExamResult {
  id: string;
  examId: string;
  studentEnrollmentId: string;
  subjectId: string;
  marksObtained: number;
  maxMarks: number;
  percentage: number;
  grade: Grade;
  resultStatus: ResultStatus;
  absent: boolean;
}

export interface ExamResultRequest {
  examId: string;
  studentEnrollmentId: string;
  subjectId: string;
  marksObtained: number;
  maxMarks: number;
  absent: boolean;
}
