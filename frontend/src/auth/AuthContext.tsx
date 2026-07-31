import { createContext, useContext, useMemo, useState, type ReactNode } from "react";

interface AuthUser {
  id?: string;
  username?: string;
  email?: string;
  roles?: string[];
}

interface AuthContextValue {
  token: string | null;
  user: AuthUser | null;
  isAdmin: boolean;
  login: (token: string, user: AuthUser) => void;
  logout: () => void;
}

const ADMIN_ROLES = ["ROLE_ADMIN", "ROLE_SUPER_ADMIN"];

const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredUser(): AuthUser | null {
  try {
    const raw = localStorage.getItem("authUser");
    return raw ? (JSON.parse(raw) as AuthUser) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() =>
    localStorage.getItem("accessToken"),
  );
  const [user, setUser] = useState<AuthUser | null>(readStoredUser);

  const value = useMemo<AuthContextValue>(
    () => ({
      token,
      user,
      isAdmin: (user?.roles ?? []).some((role) => ADMIN_ROLES.includes(role)),
      login: (newToken, newUser) => {
        localStorage.setItem("accessToken", newToken);
        localStorage.setItem("authUser", JSON.stringify(newUser));
        setToken(newToken);
        setUser(newUser);
      },
      logout: () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("authUser");
        setToken(null);
        setUser(null);
      },
    }),
    [token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
