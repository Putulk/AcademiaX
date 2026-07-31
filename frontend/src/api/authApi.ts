import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  AuthenticationResponse,
  LoginRequest,
  RegisterRequest,
  UserSummary,
} from "../types/auth";

const client = createApiClient(SERVICE_URLS.auth);
const BASE = "/api/v1/auth";

export const authApi = {
  register: (payload: RegisterRequest) =>
    client.post<AuthenticationResponse>(`${BASE}/register`, payload),
  login: (payload: LoginRequest) =>
    client.post<AuthenticationResponse>(`${BASE}/login`, payload),

  // Admin-only — requires the caller's JWT to carry ROLE_ADMIN or ROLE_SUPER_ADMIN.
  listUsers: () => client.get<UserSummary[]>(`${BASE}/users`),
  listRoles: () => client.get<string[]>(`${BASE}/roles`),
  assignRoles: (userId: string, roles: string[]) =>
    client.put<UserSummary>(`${BASE}/users/${userId}/roles`, { roles }),
};
