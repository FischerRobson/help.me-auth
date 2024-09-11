package com.helpme.auth_ms.constants;


import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class Constants {
    @Value("${security.token.secret}")
    private String secret;

    @Value("${security.token.expiration-minutes}")
    private long expirationMinutes;

    @Getter
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public Instant getJwtExpirationTime() {
        return Instant.now().plus(Duration.ofMinutes(expirationMinutes));
    }
}