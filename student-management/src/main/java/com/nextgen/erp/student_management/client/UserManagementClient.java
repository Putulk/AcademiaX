package com.nextgen.erp.student_management.client;

import com.nextgen.erp.student_management.client.dto.UserProfileResponse;
import com.nextgen.erp.student_management.exception.UserProfileNotFoundException;
import com.nextgen.erp.student_management.exception.UserServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserManagementClient {

    private final RestClient restClient;

    @Value("${services.user-management.base-url}")
    private String baseUrl;

    public UserProfileResponse getUserProfile(UUID profileId) {

        return restClient.get()
                .uri(baseUrl + "/api/v1/users/" + profileId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new UserProfileNotFoundException("User Profile not found");
                        })

                .onStatus(HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new UserServiceUnavailableException("User Management Service unavailable");
                        })
                .body(UserProfileResponse.class);
    }
}