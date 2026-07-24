package com.nextgen.erp.user_management.mapper;

import com.nextgen.erp.user_management.dto.UserProfileRequest;
import com.nextgen.erp.user_management.dto.UserProfileResponse;
import com.nextgen.erp.user_management.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfile toEntity(UserProfileRequest request) {

        return UserProfile.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .active(true)
                .build();
    }

    public UserProfileResponse toResponse(UserProfile user) {

        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .country(user.getCountry())
                .profileImage(user.getProfileImage())
                .active(user.getActive())
                .build();
    }
}
