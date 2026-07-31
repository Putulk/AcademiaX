import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { Layout } from "../components/Layout";

export function ProtectedLayout() {
  const { token } = useAuth();

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <Layout />;
}

export function PublicOnlyRoute() {
  const { token } = useAuth();

  if (token) {
    return <Navigate to="/exams" replace />;
  }

  return <Outlet />;
}
