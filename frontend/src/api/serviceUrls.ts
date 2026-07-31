export const SERVICE_URLS = {
  auth: import.meta.env.VITE_AUTH_API_BASE_URL ?? "http://localhost:8081",
  user: import.meta.env.VITE_USER_API_BASE_URL ?? "http://localhost:8082",
  student: import.meta.env.VITE_STUDENT_API_BASE_URL ?? "http://localhost:8083",
  academic: import.meta.env.VITE_ACADEMIC_API_BASE_URL ?? "http://localhost:8084",
  faculty: import.meta.env.VITE_FACULTY_API_BASE_URL ?? "http://localhost:8085",
  attendance:
    import.meta.env.VITE_ATTENDANCE_API_BASE_URL ?? "http://localhost:8087",
  examination:
    import.meta.env.VITE_EXAMINATION_API_BASE_URL ?? "http://localhost:8088",
};
