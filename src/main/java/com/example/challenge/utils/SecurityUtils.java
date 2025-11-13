package com.example.challenge.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class SecurityUtils {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Optional<String> getCurrentUserEmail() {
        Authentication auth = getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(auth.getName());
    }

    public boolean hasRole(String roleWithoutPrefix) {
        Authentication auth = getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }

        String requiredRole = "ROLE_" + roleWithoutPrefix.toUpperCase();

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredRole::equals);
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public void clear() {
        SecurityContextHolder.clearContext();
    }
}
