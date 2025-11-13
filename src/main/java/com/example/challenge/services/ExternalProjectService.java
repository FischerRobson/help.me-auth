package com.example.challenge.services;

import static com.example.challenge.utils.SecurityUtils.*;

import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.User;
import com.example.challenge.model.dto.ProjectDTO;
import com.example.challenge.repositories.ExternalProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalProjectService {

    private final ExternalProjectRepository externalProjectRepository;
    private final UserService userService;

    @Transactional
    public ProjectDTO createProject(UUID targetUserId, ExternalProject project, String currentUserId) {

        boolean isAdmin = isAdmin();

        User targetUser = userService.getUserById(targetUserId);

        if (!isAdmin && !targetUserId.toString().equals(currentUserId)) {
            log.error("Error on creating a project - user is not admin");
            throw new SecurityException("You can only create projects for your own account");
        }

        project.setUser(targetUser);
        ExternalProject saved = externalProjectRepository.save(project);
        return ProjectDTO.from(saved);
    }


    @Transactional
    public List<ProjectDTO> getProjectsByUser(UUID userId) {
        User user = userService.getUserById(userId);
        List<ExternalProject> projects = externalProjectRepository.findByUser(user);
        log.debug("Retrieving all users");
        return projects.stream().map(ProjectDTO::from).collect(Collectors.toList());
    }


    @Transactional
    public void deleteProject(UUID userId, UUID projectId) {
        ExternalProject project = externalProjectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getUser().getId().equals(userId)) {
            log.error("Can't delete a project that doesn't belong to other user");
            throw new IllegalStateException("You cannot delete a project that doesn't belong to this user");
        }

        log.debug("Project {} deleted", projectId);
        externalProjectRepository.delete(project);
    }
}