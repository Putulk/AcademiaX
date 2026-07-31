export interface AcademicYear {
  id: string;
  name: string;
  startDate: string;
  endDate: string;
  active: boolean;
}

export interface AcademicYearRequest {
  name: string;
  startDate: string;
  endDate: string;
  active: boolean;
}

export interface ClassRoom {
  id: string;
  name: string;
  displayOrder: number;
  active: boolean;
}

export interface ClassRoomRequest {
  name: string;
  displayOrder: number;
  active: boolean;
}

export interface Section {
  id: string;
  name: string;
  capacity: number;
  active: boolean;
}

export interface SectionRequest {
  name: string;
  capacity: number;
  active: boolean;
}

export type SubjectType = "CORE" | "ELECTIVE" | "OPTIONAL" | "PRACTICAL";

export const SUBJECT_TYPES: SubjectType[] = [
  "CORE",
  "ELECTIVE",
  "OPTIONAL",
  "PRACTICAL",
];

export interface Subject {
  id: string;
  name: string;
  code: string;
  type: SubjectType;
  active: boolean;
}

export interface SubjectRequest {
  name: string;
  code: string;
  type: SubjectType;
  active: boolean;
}

export interface ClassSection {
  id: string;
  academicYearId: string;
  classRoomId: string;
  sectionId: string;
  classTeacherId?: string;
  roomNumber: string;
  capacity: number;
  active: boolean;
}

export interface ClassSectionRequest {
  academicYearId: string;
  classRoomId: string;
  sectionId: string;
  classTeacherId?: string;
  roomNumber: string;
  capacity: number;
  active: boolean;
}
