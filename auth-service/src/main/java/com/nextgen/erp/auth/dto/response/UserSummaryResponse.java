package com.nextgen.erp.auth.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {

    private UUID id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> roles;
}
