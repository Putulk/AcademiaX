package com.nextgen.erp.user_management.service;

import com.nextgen.erp.user_management.dto.UserProfileRequest;
import com.nextgen.erp.user_management.dto.UserProfileResponse;

import java.util.List;
import java.util.UUID;

public interface UserProfileService{
    UserProfileResponse create(UserProfileRequest request);

    UserProfileResponse getById(UUID userId);

    UserProfileResponse update(UUID userId, UserProfileRequest request);

    void delete(UUID userId);

    List<UserProfileResponse> getAll();
}
