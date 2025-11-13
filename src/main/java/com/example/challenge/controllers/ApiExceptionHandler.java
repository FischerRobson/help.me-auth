package com.example.challenge.controllers;


import com.example.challenge.exceptions.AuthenticationFailedException;
import com.example.challenge.exceptions.UserAlreadyExistsException;
import com.example.challenge.exceptions.UserNotFoundException;
import com.example.challenge.model.ServerError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerError> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);

        ServerError error = new ServerError(
                "Unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ServerError> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);

        ServerError error = new ServerError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ServerError> handleAuthorizationDenied(AuthenticationFailedException ex, HttpServletRequest request) {
        log.error("Authentication Failed", ex);

        ServerError error = new ServerError(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ServerError> handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        log.error("Authorization denied", ex);

        ServerError error = new ServerError(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ServerError> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.error("User not found", ex);

        ServerError error = new ServerError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerError> handleMethodArgument(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                org.springframework.validation.FieldError::getField,
                                org.springframework.validation.FieldError::getDefaultMessage,
                                (existing, replacement) -> existing
                        )
                );

        log.error("Some method Argument is not valid", ex);

        ServerError error = new ServerError(
                "Validations failed",
                HttpStatus.BAD_REQUEST.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
