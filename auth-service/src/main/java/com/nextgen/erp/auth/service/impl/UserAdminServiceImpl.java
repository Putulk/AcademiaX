package com.nextgen.erp.auth.service.impl;

import com.nextgen.erp.auth.dto.request.AssignRolesRequest;
import com.nextgen.erp.auth.dto.response.UserSummaryResponse;
import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.auth.repository.UserRepository;
import com.nextgen.erp.auth.service.UserAdminService;
import com.nextgen.erp.common.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserSummaryResponse assignRoles(
            UUID userId,
            AssignRolesRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userId));

        Set<Roles> roles = new HashSet<>();

        for (RoleName roleName : request.getRoles()) {

            Roles role = roleRepository.findByName(roleName)
                    .orElseThrow(() ->
                            new RuntimeException("Role not found: " + roleName));

            roles.add(role);
        }

        user.setRoles(roles);

        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllRoleNames() {

        return java.util.Arrays.stream(RoleName.values())
                .map(Enum::name)
                .toList();
    }

    private UserSummaryResponse toResponse(User user) {

        return UserSummaryResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .toList())
                .build();
    }
}
