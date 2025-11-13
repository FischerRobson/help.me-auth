package com.example.challenge.services;

import com.auth0.jwt.JWT;
import com.example.challenge.constants.Constants;
import com.example.challenge.exceptions.AuthenticationFailedException;
import com.example.challenge.model.Login;
import com.example.challenge.model.User;
import com.example.challenge.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Constants constants;

    public String authenticate(Login login) {
        User user = this.userRepository.findByEmail(login.getEmail()).orElseThrow(AuthenticationFailedException::new);

        boolean passwordMatches = this.passwordEncoder
                .matches(login.getPassword(), user.getPassword());

        if (!passwordMatches) {
            log.error("Authentication failed, throwing exception");
            throw new AuthenticationFailedException();
        }

        String jwtId = UUID.randomUUID().toString();

        log.info("User authenticated: {}, with JWTId: {}", user.getId(), jwtId);

        return JWT.create()
                .withIssuer("helpme-auth")
                .withSubject(user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withClaim("email", user.getEmail())
                .withJWTId(jwtId)
                .withIssuedAt(new Date())
                .withExpiresAt(constants.getJwtExpirationTime())
                .sign(constants.getAlgorithm());
    }

}
