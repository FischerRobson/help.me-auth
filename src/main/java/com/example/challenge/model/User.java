package com.example.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters")
    @Column(nullable = false, length = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 120, message = "Name must be between 2 and 120 characters")
    @Column(nullable = false, length = 120)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Roles role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalProject> externalProjects = new ArrayList<>();
}
