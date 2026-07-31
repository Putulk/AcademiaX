package com.nextgen.erp.auth.dto.request;

import com.nextgen.erp.common.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private List<RoleName> roles;
}
