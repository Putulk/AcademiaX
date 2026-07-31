export interface Teacher {
  id: string;
  userProfileId: string;
  employeeCode: string;
  designation?: string;
  department?: string;
  joiningDate: string;
  experienceYears?: number;
  salary?: number;
  active: boolean;
}

export interface TeacherRequest {
  userProfileId: string;
  employeeCode: string;
  designation?: string;
  department?: string;
  joiningDate: string;
  experienceYears?: number;
  salary?: number;
  active: boolean;
}

export interface TeacherAssignment {
  id: string;
  teacherId: string;
  classSectionId: string;
  subjectId: string;
  academicYearId: string;
  active: boolean;
}

export interface TeacherAssignmentRequest {
  teacherId: string;
  classSectionId: string;
  subjectId: string;
  academicYearId: string;
  active: boolean;
}
