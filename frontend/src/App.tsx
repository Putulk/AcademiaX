import { Navigate, Route, Routes } from "react-router-dom";
import { ProtectedLayout, PublicOnlyRoute } from "./auth/RouteGuards";
import { AuthPage } from "./pages/AuthPage";
import { UserProfilesPage } from "./pages/UserProfilesPage";
import { UsersRolesPage } from "./pages/UsersRolesPage";
import { AcademicYearsPage } from "./pages/AcademicYearsPage";
import { ClassRoomsPage } from "./pages/ClassRoomsPage";
import { SectionsPage } from "./pages/SectionsPage";
import { SubjectsPage } from "./pages/SubjectsPage";
import { ClassSectionsPage } from "./pages/ClassSectionsPage";
import { TeachersPage } from "./pages/TeachersPage";
import { TeacherAssignmentsPage } from "./pages/TeacherAssignmentsPage";
import { StudentsPage } from "./pages/StudentsPage";
import { StudentEnrollmentsPage } from "./pages/StudentEnrollmentsPage";
import { AttendancePage } from "./pages/AttendancePage";
import { ExamsPage } from "./pages/ExamsPage";
import { ExamSchedulesPage } from "./pages/ExamSchedulesPage";
import { ExamResultsPage } from "./pages/ExamResultsPage";

function App() {
  return (
    <Routes>
      <Route element={<PublicOnlyRoute />}>
        <Route path="/login" element={<AuthPage />} />
      </Route>

      <Route element={<ProtectedLayout />}>
        <Route index element={<Navigate to="/exams" replace />} />

        <Route path="/user-profiles" element={<UserProfilesPage />} />
        <Route path="/admin/users" element={<UsersRolesPage />} />

        <Route path="/academic-years" element={<AcademicYearsPage />} />
        <Route path="/classes" element={<ClassRoomsPage />} />
        <Route path="/sections" element={<SectionsPage />} />
        <Route path="/subjects" element={<SubjectsPage />} />
        <Route path="/class-sections" element={<ClassSectionsPage />} />

        <Route path="/teachers" element={<TeachersPage />} />
        <Route path="/teacher-assignments" element={<TeacherAssignmentsPage />} />

        <Route path="/students" element={<StudentsPage />} />
        <Route path="/student-enrollments" element={<StudentEnrollmentsPage />} />

        <Route path="/attendance" element={<AttendancePage />} />

        <Route path="/exams" element={<ExamsPage />} />
        <Route path="/schedules" element={<ExamSchedulesPage />} />
        <Route path="/results" element={<ExamResultsPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
