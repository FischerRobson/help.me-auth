package com.helpme.auth_ms.services;

import com.auth0.jwt.JWT;
import com.helpme.auth_ms.constants.Constants;
import com.helpme.auth_ms.exceptions.AuthenticationFailedException;
import com.helpme.auth_ms.model.Login;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Constants constants;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String authenticate(Login login) {
        User user = this.userRepository.findByEmail(login.getEmail()).orElseThrow(AuthenticationFailedException::new);

        boolean passwordMatches = this.passwordEncoder
                .matches(login.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationFailedException();
        }

        String sessionId = UUID.randomUUID().toString();

        logger.info("User authenticated: {}, with session_id: {}", user.getId(), sessionId);

        String token = JWT.create()
                .withIssuer("helpme-auth")
                .withSubject(user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withClaim("email", user.getEmail())
                .withClaim("session_id", sessionId)
                .withExpiresAt(constants.getJwtExpirationTime())
                .sign(constants.getAlgorithm());

        return token;
    }

}
