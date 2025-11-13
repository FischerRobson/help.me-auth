package com.example.challenge.exceptions;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
        super("Invalid role");
    }
}
