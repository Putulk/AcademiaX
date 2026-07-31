export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface ApiErrorBody {
  message?: string;
  error?: string;
  errors?: { field: string; defaultMessage: string }[];
}
