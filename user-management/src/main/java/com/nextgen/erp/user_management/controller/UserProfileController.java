package com.nextgen.erp.user_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.user_management.dto.PageResponse;
import com.nextgen.erp.user_management.dto.UserProfileRequest;
import com.nextgen.erp.user_management.dto.UserProfileResponse;
import com.nextgen.erp.user_management.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> createUser(
            @Valid @RequestBody UserProfileRequest request) {

        UserProfileResponse response = userProfileService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUser(
            @PathVariable UUID userId) {

        UserProfileResponse response = userProfileService.getById(userId);

        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("User fetched successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserProfileResponse>>> getAllUsers(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String search) {

        PageResponse<UserProfileResponse> response =
                userProfileService.getAll(page, size, sortBy, direction, search);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<UserProfileResponse>>builder()
                        .success(true)
                        .message("Users fetched successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequest request) {

        UserProfileResponse response = userProfileService.update(userId, request);

        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("User updated successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID userId) {

        userProfileService.delete(userId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User deleted successfully")
                        .data(null)
                        .build());
    }

    @PostMapping(
            value = "/{userId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<UserProfileResponse>> uploadProfileImage(

            @PathVariable UUID userId,

            @RequestParam("file") MultipartFile file) {

        UserProfileResponse response =
                userProfileService.uploadProfileImage(userId, file);

        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("Profile image uploaded successfully")
                        .data(response)
                        .build()
        );
    }
}