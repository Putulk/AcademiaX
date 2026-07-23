package com.nextgen.erp.auth.security.config;

import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.common.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {

        return args -> {

            if (roleRepository.count() == 0) {

                createRole(RoleName.ROLE_SUPER_ADMIN, "Super Administrator");
                createRole(RoleName.ROLE_ADMIN, "Administrator");
                createRole(RoleName.ROLE_MANAGEMENT, "Management");
                createRole(RoleName.ROLE_TEACHER, "Teacher");
                createRole(RoleName.ROLE_STUDENT, "Student");
                createRole(RoleName.ROLE_PARENT, "Parent");
                createRole(RoleName.ROLE_ACCOUNTANT, "Accountant");
                createRole(RoleName.ROLE_LIBRARIAN, "Librarian");
                createRole(RoleName.ROLE_HR, "Human Resource");
            }
        };
    }

    private void createRole(RoleName roleName, String description) {

        Roles role = new Roles();
        role.setName(roleName);
        role.setDescription(description);

        roleRepository.save(role);
    }
}