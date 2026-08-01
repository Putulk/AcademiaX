import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { Layout } from "../components/Layout";
import { getAllowedSections, SECTION_HOME, type SectionKey } from "./permissions";

function homeFor(roles: string[] | undefined): string {
  const allowed = getAllowedSections(roles);
  const first = [...allowed][0];
  return first ? SECTION_HOME[first] : "/no-access";
}

export function ProtectedLayout() {
  const { token } = useAuth();

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <Layout />;
}

export function PublicOnlyRoute() {
  const { token, user } = useAuth();

  if (token) {
    return <Navigate to={homeFor(user?.roles)} replace />;
  }

  return <Outlet />;
}

export function HomeRedirect() {
  const { user } = useAuth();

  return <Navigate to={homeFor(user?.roles)} replace />;
}

export function SectionGuard({ section }: { section: SectionKey }) {
  const { user } = useAuth();
  const allowed = getAllowedSections(user?.roles);

  if (!allowed.has(section)) {
    return <Navigate to="/no-access" replace />;
  }

  return <Outlet />;
}
