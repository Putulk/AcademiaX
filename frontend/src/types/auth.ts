export interface RegisterRequest {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthenticationResponse {
  id?: string;
  accessToken?: string;
  refreshToken?: string;
  tokenType?: string;
  username?: string;
  email?: string;
  roles?: string[];
  message: string;
}

export interface UserSummary {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: string[];
}
