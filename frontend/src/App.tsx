import { Navigate, Route, Routes } from "react-router-dom";
import { HomeRedirect, ProtectedLayout, PublicOnlyRoute, SectionGuard } from "./auth/RouteGuards";
import { AuthPage } from "./pages/AuthPage";
import { NoAccessPage } from "./pages/NoAccessPage";
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
        <Route index element={<HomeRedirect />} />
        <Route path="/no-access" element={<NoAccessPage />} />

        <Route element={<SectionGuard section="Admin" />}>
          <Route path="/admin/users" element={<UsersRolesPage />} />
        </Route>

        <Route element={<SectionGuard section="Users" />}>
          <Route path="/user-profiles" element={<UserProfilesPage />} />
        </Route>

        <Route element={<SectionGuard section="Academic" />}>
          <Route path="/academic-years" element={<AcademicYearsPage />} />
          <Route path="/classes" element={<ClassRoomsPage />} />
          <Route path="/sections" element={<SectionsPage />} />
          <Route path="/subjects" element={<SubjectsPage />} />
          <Route path="/class-sections" element={<ClassSectionsPage />} />
        </Route>

        <Route element={<SectionGuard section="Faculty" />}>
          <Route path="/teachers" element={<TeachersPage />} />
          <Route path="/teacher-assignments" element={<TeacherAssignmentsPage />} />
        </Route>

        <Route element={<SectionGuard section="Students" />}>
          <Route path="/students" element={<StudentsPage />} />
          <Route path="/student-enrollments" element={<StudentEnrollmentsPage />} />
        </Route>

        <Route element={<SectionGuard section="Attendance" />}>
          <Route path="/attendance" element={<AttendancePage />} />
        </Route>

        <Route element={<SectionGuard section="Examination" />}>
          <Route path="/exams" element={<ExamsPage />} />
          <Route path="/schedules" element={<ExamSchedulesPage />} />
          <Route path="/results" element={<ExamResultsPage />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
