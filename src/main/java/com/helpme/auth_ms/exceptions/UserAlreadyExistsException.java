package com.helpme.auth_ms.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("This email is already in use");
    }

}
