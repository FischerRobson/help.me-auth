package com.helpme.auth_ms.controllers;

import com.helpme.auth_ms.model.DTO.ChangeRoleRequest;
import com.helpme.auth_ms.model.Roles;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        try {
            User savedUser = this.userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity changeRole(@PathVariable("id") UUID userId, @RequestBody ChangeRoleRequest changeRoleRequest) {
        try {
            this.userService.changeRole(userId, changeRoleRequest.getRole());
            return ResponseEntity.status(HttpStatus.OK).body("Role changed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
