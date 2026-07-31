export type BloodGroup =
  | "A_POSITIVE"
  | "A_NEGATIVE"
  | "B_POSITIVE"
  | "B_NEGATIVE"
  | "AB_POSITIVE"
  | "AB_NEGATIVE"
  | "O_POSITIVE"
  | "O_NEGATIVE";

export const BLOOD_GROUPS: BloodGroup[] = [
  "A_POSITIVE",
  "A_NEGATIVE",
  "B_POSITIVE",
  "B_NEGATIVE",
  "AB_POSITIVE",
  "AB_NEGATIVE",
  "O_POSITIVE",
  "O_NEGATIVE",
];

export type Religion =
  | "HINDUISM"
  | "ISLAM"
  | "CHRISTIANITY"
  | "SIKHISM"
  | "BUDDHISM"
  | "JAINISM"
  | "OTHER";

export const RELIGIONS: Religion[] = [
  "HINDUISM",
  "ISLAM",
  "CHRISTIANITY",
  "SIKHISM",
  "BUDDHISM",
  "JAINISM",
  "OTHER",
];

export type StudentCategory = "GENERAL" | "OBC" | "SC" | "ST" | "EWS";

export const STUDENT_CATEGORIES: StudentCategory[] = [
  "GENERAL",
  "OBC",
  "SC",
  "ST",
  "EWS",
];

export type StudentStatus = "ACTIVE" | "INACTIVE" | "SUSPENDED" | "ALUMNI";

export const STUDENT_STATUSES: StudentStatus[] = [
  "ACTIVE",
  "INACTIVE",
  "SUSPENDED",
  "ALUMNI",
];

export interface Student {
  id: string;
  userProfileId: string;
  classId?: string;
  sectionId?: string;
  admissionNumber: string;
  rollNumber: string;
  academicYear: string;
  admissionDate: string;
  bloodGroup?: BloodGroup;
  religion?: Religion;
  category?: StudentCategory;
  house?: string;
  status: StudentStatus;
}

export interface StudentRequest {
  userProfileId: string;
  classId?: string;
  sectionId?: string;
  admissionNumber: string;
  rollNumber: string;
  academicYear: string;
  admissionDate: string;
  bloodGroup?: BloodGroup;
  religion?: Religion;
  category?: StudentCategory;
  house?: string;
  status: StudentStatus;
}

export interface StudentEnrollment {
  id: string;
  studentId: string;
  academicYearId: string;
  classSectionId: string;
  rollNumber: string;
  status: StudentStatus;
  active: boolean;
}

export interface StudentEnrollmentRequest {
  studentId: string;
  academicYearId: string;
  classSectionId: string;
  rollNumber: string;
  status: StudentStatus;
  active: boolean;
}
