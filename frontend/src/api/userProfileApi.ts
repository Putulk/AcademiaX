import { createApiClient } from "./client";
import { SERVICE_URLS } from "./serviceUrls";
import type {
  PageResponse,
  UserProfile,
  UserProfileRequest,
} from "../types/userProfile";

const client = createApiClient(SERVICE_URLS.user);
const BASE = "/api/v1/users";

// The backend's UserProfileResponse has no `id` field — every lookup is keyed
// by `userId`. We alias userId -> id here so this fits the shared CrudPage's
// {id: string} shape without special-casing it.
const withId = (p: Omit<UserProfile, "id">): UserProfile => ({
  ...p,
  id: p.userId,
});

export const userProfileApi = {
  list: async () => {
    const page = await client.get<PageResponse<Omit<UserProfile, "id">>>(
      `${BASE}?page=0&size=100`,
    );
    return page.content.map(withId);
  },
  get: async (userId: string) =>
    withId(await client.get<Omit<UserProfile, "id">>(`${BASE}/${userId}`)),
  create: async (payload: UserProfileRequest) =>
    withId(await client.post<Omit<UserProfile, "id">>(BASE, payload)),
  update: async (userId: string, payload: UserProfileRequest) =>
    withId(
      await client.put<Omit<UserProfile, "id">>(`${BASE}/${userId}`, payload),
    ),
  remove: (userId: string) => client.delete<void>(`${BASE}/${userId}`),
};
