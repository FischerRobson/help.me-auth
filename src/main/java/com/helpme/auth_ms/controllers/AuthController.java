package com.helpme.auth_ms.controllers;

import com.helpme.auth_ms.model.Login;
import com.helpme.auth_ms.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Login login) {
        try {
            String token = this.authService.authenticate(login);
            return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
