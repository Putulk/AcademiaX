package com.nextgen.erp.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Application is running";
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String student() {
        return "Welcome Student";
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public String teacher() {
        return "Welcome Teacher";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Welcome Admin";
    }

    @GetMapping("/management")
    @PreAuthorize("hasRole('MANAGEMENT')")
    public String management() {
        return "Welcome Management";
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {

        Map<String, Object> map = new HashMap<>();

        map.put("authentication", authentication);
        map.put("name", authentication.getName());
        map.put("authorities", authentication.getAuthorities());

        return map;
    }
}
