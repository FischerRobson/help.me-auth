package com.helpme.auth_ms.exceptions;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
        super("Authentication Failed");
    }

}
