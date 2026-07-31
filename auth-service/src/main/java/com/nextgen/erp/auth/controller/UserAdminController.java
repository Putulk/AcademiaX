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

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {

        return ResponseEntity.ok(userAdminService.getAllUsers());
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
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
