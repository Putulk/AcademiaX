package com.nextgen.erp.auth.service;

import com.nextgen.erp.auth.dto.request.AssignRolesRequest;
import com.nextgen.erp.auth.dto.response.UserSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface UserAdminService {

    List<UserSummaryResponse> getAllUsers();

    UserSummaryResponse assignRoles(UUID userId, AssignRolesRequest request);

    List<String> getAllRoleNames();
}
