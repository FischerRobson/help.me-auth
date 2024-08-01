package com.helpme.auth_ms.model;

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
}
