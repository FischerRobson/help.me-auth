package com.example.challenge.model;

import com.example.challenge.exceptions.InvalidRoleException;

public enum Roles {
    USER("user"),
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
