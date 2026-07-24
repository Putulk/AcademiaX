package com.nextgen.erp.user_management.service;

import com.nextgen.erp.user_management.dto.PageResponse;
import com.nextgen.erp.user_management.dto.UserProfileRequest;
import com.nextgen.erp.user_management.dto.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserProfileService{
    UserProfileResponse create(UserProfileRequest request);

    UserProfileResponse getById(UUID userId);

    UserProfileResponse update(UUID userId,
                               UserProfileRequest request);

    void delete(UUID userId);

    PageResponse<UserProfileResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String search
    );
}
