package com.helpme.auth_ms.model;

import com.helpme.auth_ms.exceptions.InvalidRoleException;

public enum Roles {
    USER("user"),
    SUPPORT("support"),
    ADMIN("admin");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Roles fromString(String roleStr) {
        for (Roles role : Roles.values()) {
            if (role.getRole().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new InvalidRoleException();
    }
}
