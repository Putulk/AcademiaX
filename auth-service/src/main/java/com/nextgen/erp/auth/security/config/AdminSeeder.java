package com.nextgen.erp.auth.security.config;

import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import com.nextgen.erp.auth.repository.RoleRepository;
import com.nextgen.erp.auth.repository.UserRepository;
import com.nextgen.erp.common.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Split out from DataInitializer so this runs inside a real transaction.
 * Roles uses Lombok @Data, whose generated hashCode()/equals() touch the
 * lazy `users` collection — adding a managed Roles instance to a HashSet
 * outside an active session throws LazyInitializationException. A
 * CommandLineRunner isn't transactional, and self-invocation from within
 * DataInitializer would bypass @Transactional anyway, so this needs to be
 * a separate bean called from outside.
 */
@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@academiax.local";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin@123";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void seedDefaultAdminIfMissing() {

        if (userRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
            return;
        }

        Roles adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException("ROLE_ADMIN was not seeded."));

        Set<Roles> roles = new HashSet<>();
        roles.add(adminRole);

        User admin = User.builder()
                .username("admin")
                .firstName("System")
                .lastName("Admin")
                .email(DEFAULT_ADMIN_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(roles)
                .build();

        userRepository.save(admin);
    }
}
