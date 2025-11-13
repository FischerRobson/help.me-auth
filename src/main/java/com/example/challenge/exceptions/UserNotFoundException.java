package com.example.challenge.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found");
    }
}
