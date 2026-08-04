package com.nextgen.erp.common.security.autoconfigure;

import com.nextgen.erp.common.security.AccessDeniedExceptionHandler;
import com.nextgen.erp.common.security.JwtAuthenticationEntryPoint;
import com.nextgen.erp.common.security.jwt.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Auto-wires stateless JWT authentication for any service that depends on
 * {@code common} and has {@code spring-boot-starter-security} on its
 * classpath (transitively supplied by this module). Any service that
 * defines its own {@link SecurityFilterChain} (auth-service) is left
 * completely alone — the whole bundle below is gated as one unit on the
 * nested config class, not bean-by-bean, so auth-service never even
 * registers our filter/entry-point beans (which would otherwise collide by
 * bean *name*, not type, with its own identically-named classes).
 */
@AutoConfiguration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@ConditionalOnClass(OncePerRequestFilter.class)
public class JwtResourceServerAutoConfiguration {

    /**
     * Registered unconditionally (not gated on the absence of a
     * SecurityFilterChain) — auth-service uses {@code @PreAuthorize} too
     * (TestController/UserAdminController) and benefits from the same fix,
     * and it has no competing catch-all of its own to conflict with.
     */
    @Bean
    @ConditionalOnMissingBean(AccessDeniedExceptionHandler.class)
    AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    @EnableMethodSecurity
    static class JwtSecurityConfiguration {

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter();
        }

        @Bean
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
            return new JwtAuthenticationEntryPoint();
        }

        /**
         * Stateless JWT auth builds Authentication from token claims alone —
         * there's no username/password login here, so this exists only to
         * stop Boot's own UserDetailsServiceAutoConfiguration from
         * generating a throwaway in-memory user and printing a password to
         * the log.
         */
        @Bean
        @ConditionalOnMissingBean(UserDetailsService.class)
        UserDetailsService noOpUserDetailsService() {
            return username -> {
                throw new UsernameNotFoundException("Stateless JWT auth — user lookup not supported here");
            };
        }

        @Bean
        SecurityFilterChain jwtSecurityFilterChain(
                HttpSecurity http,
                JwtAuthenticationFilter jwtAuthenticationFilter,
                JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(exception ->
                            exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
    }
}
