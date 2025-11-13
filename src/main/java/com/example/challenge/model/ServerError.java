package com.example.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ServerError {
    private String message;
    private int code;
    private String method;
    private String path;
    private Instant timestamp;
    private Map<String, String> details;
}