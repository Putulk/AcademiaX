package com.nextgen.erp.auth.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private UUID id;

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private String username;

    private String email;

    private List<String> roles;

    private String message;

}