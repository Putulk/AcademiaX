package com.nextgen.erp.auth.service.impl;

import com.nextgen.erp.auth.dto.request.RegisterRequest;
import com.nextgen.erp.auth.dto.response.AuthenticationResponse;
import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import com.nextgen.erp.auth.enums.RoleName;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.auth.repository.UserRepository;
import com.nextgen.erp.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }

        Roles studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() ->
                        new RuntimeException("Default role ROLE_STUDENT not found."));

        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        Set<Roles> roles = new HashSet<>();
        roles.add(studentRole);

        user.setRoles(roles);

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .message("User registered successfully.")
                .build();
    }
}