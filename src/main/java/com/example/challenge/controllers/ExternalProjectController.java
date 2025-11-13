package com.example.challenge.controllers;

import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.dto.ProjectDTO;
import com.example.challenge.services.ExternalProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/external-projects/{userId}")
@RequiredArgsConstructor
public class ExternalProjectController {

    private final ExternalProjectService externalProjectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            HttpServletRequest request,
            @PathVariable UUID userId,
            @RequestBody ExternalProject project
    ) {
        return ResponseEntity.ok(externalProjectService.createProject(userId, project, (String) request.getAttribute("userId")));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> listUserProjects(@PathVariable UUID userId) {
        return ResponseEntity.ok(externalProjectService.getProjectsByUser(userId));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable UUID userId,
            @PathVariable UUID projectId
    ) {
        externalProjectService.deleteProject(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}