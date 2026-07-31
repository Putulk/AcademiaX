import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "../api/authApi";
import { ApiError } from "../api/client";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import { useAuth } from "../auth/AuthContext";
import type { LoginRequest, RegisterRequest } from "../types/auth";

const EMPTY_REGISTER: RegisterRequest = {
  username: "",
  firstName: "",
  lastName: "",
  email: "",
  password: "",
};

const EMPTY_LOGIN: LoginRequest = { email: "", password: "" };

export function AuthPage() {
  const [tab, setTab] = useState<"login" | "register">("login");
  const [registerForm, setRegisterForm] = useState(EMPTY_REGISTER);
  const [loginForm, setLoginForm] = useState(EMPTY_LOGIN);
  const [busy, setBusy] = useState(false);
  const [registeredId, setRegisteredId] = useState<string | null>(null);
  const toasts = useToasts();
  const auth = useAuth();
  const navigate = useNavigate();

  const submitRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setBusy(true);
    try {
      const res = await authApi.register(registerForm);
      setRegisteredId(res.id ?? null);
      toasts.success("Account created — log in below to continue.");
      setLoginForm({ email: registerForm.email, password: "" });
      setTab("login");
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Registration failed");
    } finally {
      setBusy(false);
    }
  };

  const copyId = async () => {
    if (!registeredId) return;
    await navigator.clipboard.writeText(registeredId);
    toasts.success("Copied to clipboard");
  };

  const submitLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setBusy(true);
    try {
      const res = await authApi.login(loginForm);
      if (!res.accessToken) {
        toasts.error(res.message || "Login did not return a token");
        return;
      }
      auth.login(res.accessToken, {
        id: res.id,
        username: res.username,
        email: res.email,
        roles: res.roles,
      });
      navigate("/exams", { replace: true });
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Login failed");
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-screen">
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="auth-card">
        <div className="auth-card__brand">
          <span className="sidebar__brand-mark">Ax</span>
          <span className="auth-card__brand-name">AcademiaX</span>
        </div>

        <div className="auth-card__tabs">
          <button
            type="button"
            className={`auth-tab${tab === "login" ? " auth-tab--active" : ""}`}
            onClick={() => setTab("login")}
          >
            Log In
          </button>
          <button
            type="button"
            className={`auth-tab${tab === "register" ? " auth-tab--active" : ""}`}
            onClick={() => setTab("register")}
          >
            Register
          </button>
        </div>

        {registeredId && (
          <div className="auth-id-box">
            <div>
              <div className="auth-id-box__label">
                Account created. This is its User ID — you'll need it to create a
                User Profile, Student, or Teacher record for this account:
              </div>
              <div className="cell-mono">{registeredId}</div>
            </div>
            <button type="button" className="btn btn--ghost btn--sm" onClick={copyId}>
              Copy
            </button>
          </div>
        )}

        {tab === "login" ? (
          <form onSubmit={submitLogin} className="form">
            <label>
              Email
              <input
                required
                type="email"
                value={loginForm.email}
                onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })}
              />
            </label>
            <label>
              Password
              <input
                required
                type="password"
                value={loginForm.password}
                onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
              />
            </label>
            <button type="submit" className="btn btn--primary btn--block" disabled={busy}>
              {busy ? "Logging in…" : "Log In"}
            </button>
          </form>
        ) : (
          <form onSubmit={submitRegister} className="form">
            <div className="form-row">
              <label>
                First Name
                <input
                  required
                  value={registerForm.firstName}
                  onChange={(e) =>
                    setRegisterForm({ ...registerForm, firstName: e.target.value })
                  }
                />
              </label>
              <label>
                Last Name
                <input
                  required
                  value={registerForm.lastName}
                  onChange={(e) =>
                    setRegisterForm({ ...registerForm, lastName: e.target.value })
                  }
                />
              </label>
            </div>
            <label>
              Username
              <input
                required
                value={registerForm.username}
                onChange={(e) =>
                  setRegisterForm({ ...registerForm, username: e.target.value })
                }
              />
            </label>
            <label>
              Email
              <input
                required
                type="email"
                value={registerForm.email}
                onChange={(e) =>
                  setRegisterForm({ ...registerForm, email: e.target.value })
                }
              />
            </label>
            <label>
              Password
              <input
                required
                minLength={8}
                type="password"
                value={registerForm.password}
                onChange={(e) =>
                  setRegisterForm({ ...registerForm, password: e.target.value })
                }
              />
            </label>
            <p className="form-hint">
              New accounts are created with the Student role by default.
            </p>
            <button type="submit" className="btn btn--primary btn--block" disabled={busy}>
              {busy ? "Registering…" : "Create Account"}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
