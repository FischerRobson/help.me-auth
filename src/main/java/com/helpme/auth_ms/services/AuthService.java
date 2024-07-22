package com.helpme.auth_ms.services;

import com.auth0.jwt.JWT;
import com.helpme.auth_ms.constants.Constants;
import com.helpme.auth_ms.exceptions.AuthenticationFailedException;
import com.helpme.auth_ms.model.Login;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    public String authenticate(Login login) {
        User user = this.userRepository.findByEmail(login.getEmail()).orElseThrow(AuthenticationFailedException::new);

//        boolean passwordMatches = this.passwordEncoder
//                .matches(login.getPassword(), user.getPassword());

//        if (!passwordMatches) {
//            throw new AuthenticationFailedException();
//        }

        Instant expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        String token = JWT.create()
                .withIssuer("helpme-auth")
                .withSubject(user.getId().toString())
                .withExpiresAt(expiresIn)
                .sign(Constants.algorithm);

        return token;
    }

}
