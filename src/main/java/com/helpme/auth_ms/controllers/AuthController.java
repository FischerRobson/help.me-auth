package com.helpme.auth_ms.controllers;

import com.helpme.auth_ms.model.Login;
import com.helpme.auth_ms.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    public void authenticate(@RequestBody Login login) {
        this.authService.authenticate(login);
    }

}
