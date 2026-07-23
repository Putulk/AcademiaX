package com.nextgen.erp.auth.service.impl;

import com.nextgen.erp.auth.dto.request.LoginRequest;
import com.nextgen.erp.auth.dto.request.LogoutRequest;
import com.nextgen.erp.auth.dto.request.RefreshToken;
import com.nextgen.erp.auth.dto.request.RegisterRequest;
import com.nextgen.erp.auth.dto.response.AuthenticationResponse;
import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import com.nextgen.erp.auth.exceptions.InvalidRefreshTokenException;
import com.nextgen.erp.auth.repository.RefreshTokenRepository;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.auth.repository.UserRepository;
import com.nextgen.erp.auth.security.jwt.JwtService;
import com.nextgen.erp.auth.security.service.CustomUserDetails;
import com.nextgen.erp.auth.security.service.CustomUserDetailsService;
import com.nextgen.erp.auth.service.AuthenticationService;
import com.nextgen.erp.common.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final RefreshTokenRepository refreshTokenRepository;

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

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        RefreshToken entity = RefreshToken.builder()
                .token(refreshToken)
                .user(userDetails.getUser())
                .revoked(false)
                .expired(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(entity);

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
    @Transactional
    public AuthenticationResponse refreshToken(RefreshToken request) {
        System.out.println("Inside Refresh Service");
        String oldRefreshToken = request.getToken();

        // Find Refresh Token in DB
        RefreshToken tokenEntity = refreshTokenRepository
                .findByToken(oldRefreshToken)
                .orElseThrow(() ->
                        new RuntimeException("Refresh Token not found"));

        // Check if revoked
        if (Boolean.TRUE.equals(tokenEntity.getRevoked())) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked.");
        }

        // Check if expired in DB
        if (Boolean.TRUE.equals(tokenEntity.getExpired())) {
            throw new RuntimeException("Refresh Token has expired.");
        }

        User user = tokenEntity.getUser();

        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService
                        .loadUserByUsername(user.getEmail());

        // Validate JWT
        if (!jwtService.isTokenValid(oldRefreshToken, userDetails)) {

            tokenEntity.setExpired(true);

            refreshTokenRepository.save(tokenEntity);

            throw new RuntimeException("Invalid Refresh Token.");
        }

        // Generate New Tokens
        String newAccessToken =
                jwtService.generateAccessToken(userDetails);

        String newRefreshToken =
                jwtService.generateRefreshToken(userDetails);

        // Revoke Old Refresh Token
        tokenEntity.setRevoked(true);
        tokenEntity.setExpired(true);

        refreshTokenRepository.save(tokenEntity);

        // Save New Refresh Token
        RefreshToken refreshToken = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .revoked(false)
                .expired(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Access Token refreshed successfully.")
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new RuntimeException("Refresh Token not found"));

        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);

        refreshTokenRepository.save(refreshToken);
    }
}