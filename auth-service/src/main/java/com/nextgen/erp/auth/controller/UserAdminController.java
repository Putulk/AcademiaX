package com.nextgen.erp.auth.controller;

import com.nextgen.erp.auth.dto.request.AssignRolesRequest;
import com.nextgen.erp.auth.dto.response.UserSummaryResponse;
import com.nextgen.erp.auth.service.UserAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    // Read-only, open to any authenticated user (not just admins) — the
    // User Profiles page needs this list to let non-admin roles (e.g.
    // Management) pick a userId by name instead of pasting a raw UUID.
    // Role assignment itself (below) stays admin-only.
    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {

        return ResponseEntity.ok(userAdminService.getAllUsers());
    }

    @GetMapping("/roles")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getAllRoles() {

        return ResponseEntity.ok(userAdminService.getAllRoleNames());
    }

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<UserSummaryResponse> assignRoles(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRolesRequest request) {

        return ResponseEntity.ok(userAdminService.assignRoles(id, request));
    }
}
