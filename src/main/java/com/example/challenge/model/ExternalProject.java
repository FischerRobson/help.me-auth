package com.example.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Table(name = "external_projects")
@Entity
public class ExternalProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 120, message = "Name must be between 2 and 32 characters")
    private String name;
}