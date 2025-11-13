package com.example.challenge.model.dto;

import com.example.challenge.model.ExternalProject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProjectDTO {
    private UUID id;
    private String name;

    public static ProjectDTO from(ExternalProject externalProject) {
        return new ProjectDTO(externalProject.getId(), externalProject.getName());
    }
}
