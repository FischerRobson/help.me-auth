package com.example.challenge.exceptions;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
        super("Authentication Failed");
    }

}
