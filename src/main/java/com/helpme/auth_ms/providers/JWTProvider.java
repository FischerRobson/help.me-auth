package com.helpme.auth_ms.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.helpme.auth_ms.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTProvider {

    @Autowired
    Constants constants;

    public String validateJWT(String token) {
        token = token.replace("Bearer ", "");

        try {
            return JWT.require(constants.getAlgorithm()).build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return "";
        }
    }

}