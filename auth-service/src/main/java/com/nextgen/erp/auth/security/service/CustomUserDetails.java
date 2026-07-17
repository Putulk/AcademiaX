package com.nextgen.erp.auth.security.service;

import com.nextgen.erp.auth.entity.Permission;
import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.auth.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Roles role : user.getRoles()) {

            authorities.add(
                    new SimpleGrantedAuthority(role.getName().name())
            );

            if (role.getPermissions() != null) {
                for (Permission permission : role.getPermissions()) {
                    authorities.add(
                            new SimpleGrantedAuthority(permission.getName().name())
                    );
                }
            }
        }

        System.out.println("Authorities : " + authorities);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Spring Security login username.
     * We are using Email as username.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getDisplayUsername() {
        return user.getUsername();
    }

    public UUID getUserId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getAccountNonExpired());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getAccountNonLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getCredentialsNonExpired());
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }
}