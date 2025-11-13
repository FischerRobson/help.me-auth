package com.example.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserUpdateDTO {
    private String name;
    private String email;
    private String password;
}
