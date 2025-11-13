package com.example.challenge.controllers;

import com.example.challenge.model.dto.ChangeRoleRequest;
import com.example.challenge.model.dto.UserResponseDTO;
import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.model.dto.UserUpdateDTO;
import com.example.challenge.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{id}/roles")
    public ResponseEntity<String> changeRole(@PathVariable("id") UUID userId, @RequestBody ChangeRoleRequest changeRoleRequest) {
        Roles checkedRole = Roles.fromString(changeRoleRequest.getRole());
        this.userService.changeRole(userId, checkedRole);
        return ResponseEntity.status(HttpStatus.OK).body("Role changed");
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDTO updatedUser
    ) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(UserResponseDTO.from(user));
    }
}
