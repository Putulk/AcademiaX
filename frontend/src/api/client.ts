import type { ApiErrorBody, ApiResponse } from "../types/common";

export class ApiError extends Error {
  status: number;

  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

function isWrapped<T>(body: unknown): body is ApiResponse<T> {
  return (
    !!body &&
    typeof body === "object" &&
    "success" in body &&
    "data" in body
  );
}

export function createApiClient(baseUrl: string, defaultHeaders?: Record<string, string>) {
  async function request<T>(path: string, options?: RequestInit): Promise<T> {
    const token = localStorage.getItem("accessToken");

    const response = await fetch(`${baseUrl}${path}`, {
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...defaultHeaders,
      },
      ...options,
    });

    const text = await response.text();
    const body = text ? JSON.parse(text) : null;

    if (!response.ok) {
      const errorBody = body as ApiErrorBody | null;
      const fieldErrors = errorBody?.errors
        ?.map((e) => `${e.field}: ${e.defaultMessage}`)
        .join(", ");

      const message =
        fieldErrors ||
        errorBody?.message ||
        errorBody?.error ||
        `Request failed with status ${response.status}`;

      throw new ApiError(message, response.status);
    }

    // Some endpoints in this backend wrap payloads in ApiResponse<T>
    // ({success, message, data}), others return the DTO/list directly.
    return isWrapped<T>(body) ? body.data : (body as T);
  }

  return {
    get: <T>(path: string) => request<T>(path),

    post: <T>(path: string, payload?: unknown) =>
      request<T>(path, {
        method: "POST",
        body: payload !== undefined ? JSON.stringify(payload) : undefined,
      }),

    put: <T>(path: string, payload: unknown) =>
      request<T>(path, { method: "PUT", body: JSON.stringify(payload) }),

    patch: <T>(path: string, payload?: unknown) =>
      request<T>(path, {
        method: "PATCH",
        body: payload !== undefined ? JSON.stringify(payload) : undefined,
      }),

    delete: <T>(path: string) => request<T>(path, { method: "DELETE" }),
  };
}
