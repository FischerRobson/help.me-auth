package com.example.challenge.repositories;

import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExternalProjectRepository extends JpaRepository<ExternalProject, UUID> {
    List<ExternalProject> findByUser(User user);
}
