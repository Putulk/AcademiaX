export type Gender = "MALE" | "FEMALE" | "OTHER";

export const GENDERS: Gender[] = ["MALE", "FEMALE", "OTHER"];

export interface UserProfile {
  /** Not returned by the backend — aliased from userId so this fits the shared CrudPage's {id} shape. */
  id: string;
  userId: string;
  firstName: string;
  lastName: string;
  phone?: string;
  gender?: Gender;
  dateOfBirth?: string;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  profileImage?: string;
  active: boolean;
}

export interface UserProfileRequest {
  userId: string;
  firstName: string;
  lastName: string;
  phone?: string;
  gender?: Gender;
  dateOfBirth?: string;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  active: boolean;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
