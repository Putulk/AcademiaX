export type AttendanceStatus = "PRESENT" | "ABSENT" | "LATE" | "HALF_DAY" | "LEAVE";

export const ATTENDANCE_STATUSES: AttendanceStatus[] = [
  "PRESENT",
  "ABSENT",
  "LATE",
  "HALF_DAY",
  "LEAVE",
];

export interface Attendance {
  id: string;
  studentEnrollmentId: string;
  classId: string;
  sectionId: string;
  subjectId: string;
  teacherId: string;
  attendanceDate: string;
  status: AttendanceStatus;
  remarks?: string;
}

export interface AttendanceRequest {
  studentEnrollmentId: string;
  classId: string;
  sectionId: string;
  subjectId: string;
  teacherId: string;
  attendanceDate: string;
  status: AttendanceStatus;
  remarks?: string;
}
