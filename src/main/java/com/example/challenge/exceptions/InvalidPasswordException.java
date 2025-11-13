package com.example.challenge.exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Password length must be greater than 8 and smaller than 16");
    }

}
