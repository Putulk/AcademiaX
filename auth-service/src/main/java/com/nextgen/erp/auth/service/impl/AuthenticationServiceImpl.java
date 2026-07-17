package com.nextgen.erp.auth.service.impl;

import com.nextgen.erp.auth.dto.request.LoginRequest;
import com.nextgen.erp.auth.dto.request.RefreshTokenRequest;
import com.nextgen.erp.auth.dto.request.RegisterRequest;
import com.nextgen.erp.auth.dto.response.AuthenticationResponse;
import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import com.nextgen.erp.auth.enums.RoleName;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.auth.repository.UserRepository;
import com.nextgen.erp.auth.security.jwt.JwtService;
import com.nextgen.erp.auth.security.service.CustomUserDetails;
import com.nextgen.erp.auth.security.service.CustomUserDetailsService;
import com.nextgen.erp.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

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

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(userDetails.getDisplayUsername())
                .email(userDetails.getEmail())
                .message("Login Successful")
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(
            RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        String email = jwtService.extractUsername(refreshToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .message("Access token generated successfully")
                .build();
    }
}