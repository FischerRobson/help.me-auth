package com.example.challenge.controllers;

import com.example.challenge.model.Login;
import com.example.challenge.model.User;
import com.example.challenge.services.AuthService;
import com.example.challenge.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Login login) {
        String token = this.authService.authenticate(login);
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(3600)
                .build();
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).header("Set-Cookie", cookie.toString()).build();
    }

    @PostMapping("new-user")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        User savedUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/me")
    public String getCurrentUser(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        String role = (String) request.getAttribute("userRole");
        String userId = (String) request.getAttribute("userId");

        return String.format("User: %s (role: %s, id: %s)", email, role, userId);
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).header("Set-Cookie", cookie.toString()).build();
    }

}
