package com.example.challenge.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("This email is already in use");
    }

}
