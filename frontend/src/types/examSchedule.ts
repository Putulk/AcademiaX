export interface ExamSchedule {
  id: string;
  examId: string;
  subjectId: string;
  teacherId: string;
  classId: string;
  sectionId: string;
  examDate: string;
  startTime: string;
  endTime: string;
  roomNumber?: string;
  maxMarks: number;
}

export interface ExamScheduleRequest {
  examId: string;
  subjectId: string;
  teacherId: string;
  classId: string;
  sectionId: string;
  examDate: string;
  startTime: string;
  endTime: string;
  roomNumber?: string;
  maxMarks: number;
}
