package com.example.challenge.model.dto;


import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String name;
    private Roles role;

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}
