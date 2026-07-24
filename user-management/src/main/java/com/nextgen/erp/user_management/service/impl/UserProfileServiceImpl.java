package com.nextgen.erp.user_management.service.impl;

import com.nextgen.erp.user_management.dto.PageResponse;
import com.nextgen.erp.user_management.dto.UserProfileRequest;
import com.nextgen.erp.user_management.dto.UserProfileResponse;
import com.nextgen.erp.user_management.entity.UserProfile;
import com.nextgen.erp.user_management.exception.ResourceNotFoundException;
import com.nextgen.erp.user_management.repository.UserProfileRepository;
import com.nextgen.erp.user_management.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService{

    private final UserProfileRepository repository;

    @Override
    public UserProfileResponse create(UserProfileRequest request) {

        if(repository.existsByUserId(request.getUserId())){
            throw new RuntimeException("Profile already exists");
        }

        UserProfile profile = UserProfile.builder()
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

        repository.save(profile);

        return map(profile);
    }

    @Override
    public UserProfileResponse getById(UUID userId) {

        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Profile not found"));

        return map(profile);
    }

    @Override
    public PageResponse<UserProfileResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String search) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserProfile> profilePage;

        if (search == null || search.isBlank()) {
            profilePage = repository.findAll(pageable);
        } else {
            profilePage = repository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            search,
                            search,
                            pageable);
        }

        List<UserProfileResponse> users = profilePage.getContent()
                .stream()
                .map(this::map)
                .toList();

        return PageResponse.<UserProfileResponse>builder()
                .content(users)
                .page(profilePage.getNumber())
                .size(profilePage.getSize())
                .totalElements(profilePage.getTotalElements())
                .totalPages(profilePage.getTotalPages())
                .first(profilePage.isFirst())
                .last(profilePage.isLast())
                .build();
    }

    @Override
    public UserProfileResponse update(UUID userId, UserProfileRequest request) {

        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Profile not found"));

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setGender(request.getGender());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setCountry(request.getCountry());

        repository.save(profile);

        return map(profile);
    }

    @Override
    public void delete(UUID userId) {

        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Profile not found"));

        repository.delete(profile);
    }

    private UserProfileResponse map(UserProfile profile){

        return UserProfileResponse.builder()
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .gender(profile.getGender())
                .dateOfBirth(profile.getDateOfBirth())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .profileImage(profile.getProfileImage())
                .active(profile.getActive())
                .build();
    }
}