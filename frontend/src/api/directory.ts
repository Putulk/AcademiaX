import { academicYearApi, classRoomApi, classSectionApi, sectionApi, subjectApi } from "./academicApi";
import { teacherApi } from "./facultyApi";
import { studentApi, studentEnrollmentApi } from "./studentApi";
import { userProfileApi } from "./userProfileApi";
import { examApi } from "./examApi";
import { authApi } from "./authApi";

export interface ReferenceOption {
  value: string;
  label: string;
}

export async function loadAuthUserOptions(): Promise<ReferenceOption[]> {
  const users = await authApi.listUsers();
  return users.map((u) => ({
    value: u.id,
    label: `${u.firstName} ${u.lastName} (${u.username})`,
  }));
}

export async function loadUserProfileOptions(): Promise<ReferenceOption[]> {
  const profiles = await userProfileApi.list();
  return profiles.map((p) => ({
    value: p.userId,
    label: `${p.firstName} ${p.lastName}`,
  }));
}

export async function loadClassRoomOptions(): Promise<ReferenceOption[]> {
  const rows = await classRoomApi.list();
  return rows.map((r) => ({ value: r.id, label: r.name }));
}

export async function loadSectionOptions(): Promise<ReferenceOption[]> {
  const rows = await sectionApi.list();
  return rows.map((r) => ({ value: r.id, label: r.name }));
}

export async function loadSubjectOptions(): Promise<ReferenceOption[]> {
  const rows = await subjectApi.list();
  return rows.map((r) => ({ value: r.id, label: `${r.name} (${r.code})` }));
}

export async function loadAcademicYearOptions(): Promise<ReferenceOption[]> {
  const rows = await academicYearApi.list();
  return rows.map((r) => ({ value: r.id, label: r.name }));
}

export async function loadExamOptions(): Promise<ReferenceOption[]> {
  const rows = await examApi.list();
  return rows.map((r) => ({ value: r.id, label: `${r.name} (${r.academicYear})` }));
}

export interface EnrichedClassSection {
  id: string;
  classRoomId: string;
  sectionId: string;
  academicYearId: string;
  label: string;
}

export async function loadEnrichedClassSections(): Promise<EnrichedClassSection[]> {
  const [classSections, classRooms, sections, academicYears] = await Promise.all([
    classSectionApi.list(),
    classRoomApi.list(),
    sectionApi.list(),
    academicYearApi.list(),
  ]);

  const classRoomMap = new Map(classRooms.map((c) => [c.id, c.name]));
  const sectionMap = new Map(sections.map((s) => [s.id, s.name]));
  const yearMap = new Map(academicYears.map((y) => [y.id, y.name]));

  return classSections.map((cs) => ({
    id: cs.id,
    classRoomId: cs.classRoomId,
    sectionId: cs.sectionId,
    academicYearId: cs.academicYearId,
    label: `${classRoomMap.get(cs.classRoomId) ?? "?"} - ${
      sectionMap.get(cs.sectionId) ?? "?"
    } (${yearMap.get(cs.academicYearId) ?? "?"})`,
  }));
}

export async function loadClassSectionOptions(): Promise<ReferenceOption[]> {
  const classSections = await loadEnrichedClassSections();
  return classSections.map((cs) => ({ value: cs.id, label: cs.label }));
}

export async function loadTeacherOptions(): Promise<ReferenceOption[]> {
  const [teachers, profiles] = await Promise.all([teacherApi.list(), userProfileApi.list()]);
  const profileMap = new Map(profiles.map((p) => [p.userId, `${p.firstName} ${p.lastName}`]));

  return teachers.map((t) => ({
    value: t.id,
    label: `${profileMap.get(t.userProfileId) ?? "Unknown"} (${t.employeeCode})`,
  }));
}

export async function loadStudentOptions(): Promise<ReferenceOption[]> {
  const [students, profiles] = await Promise.all([studentApi.list(), userProfileApi.list()]);
  const profileMap = new Map(profiles.map((p) => [p.userId, `${p.firstName} ${p.lastName}`]));

  return students.map((s) => ({
    value: s.id,
    label: `${profileMap.get(s.userProfileId) ?? "Unknown"} (${s.admissionNumber})`,
  }));
}

export interface EnrichedEnrollment {
  id: string;
  studentId: string;
  classSectionId: string;
  rollNumber: string;
  name: string;
}

export async function loadEnrichedStudentEnrollments(): Promise<EnrichedEnrollment[]> {
  const [enrollments, students, profiles] = await Promise.all([
    studentEnrollmentApi.list(),
    studentApi.list(),
    userProfileApi.list(),
  ]);

  const profileMap = new Map(profiles.map((p) => [p.userId, `${p.firstName} ${p.lastName}`]));
  const studentMap = new Map(students.map((s) => [s.id, s]));

  return enrollments.map((e) => {
    const student = studentMap.get(e.studentId);
    const name = student ? (profileMap.get(student.userProfileId) ?? "Unknown") : "Unknown";
    return {
      id: e.id,
      studentId: e.studentId,
      classSectionId: e.classSectionId,
      rollNumber: e.rollNumber,
      name,
    };
  });
}

export async function loadStudentEnrollmentOptions(): Promise<ReferenceOption[]> {
  const enrollments = await loadEnrichedStudentEnrollments();
  return enrollments.map((e) => ({
    value: e.id,
    label: `${e.name} — Roll ${e.rollNumber}`,
  }));
}
