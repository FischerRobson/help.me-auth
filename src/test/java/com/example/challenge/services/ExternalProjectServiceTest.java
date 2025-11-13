package com.example.challenge.services;

import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.model.dto.ProjectDTO;
import com.example.challenge.repositories.ExternalProjectRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.*;

import static com.example.challenge.utils.SecurityUtils.isAdmin;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalProjectServiceTest {

    private ExternalProjectRepository externalProjectRepository;
    private UserService userService;
    private ExternalProjectService externalProjectService;

    MockedStatic<com.example.challenge.utils.SecurityUtils> securityUtilsMock;

    @BeforeEach
    void setup() {
        externalProjectRepository = mock(ExternalProjectRepository.class);
        userService = mock(UserService.class);

        externalProjectService = new ExternalProjectService(
                externalProjectRepository,
                userService
        );

        // mock static SecurityUtils.isAdmin()
        securityUtilsMock = mockStatic(com.example.challenge.utils.SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        securityUtilsMock.close();
    }

    private User mockUser(UUID id, Roles role) {
        User u = new User();
        u.setId(id);
        u.setRole(role);
        return u;
    }

    private ExternalProject mockProject(UUID id, User user, String name) {
        ExternalProject p = new ExternalProject();
        p.setId(id);
        p.setName(name);
        p.setUser(user);
        return p;
    }

    // ------------------------------------------------------------------------
    // CREATE PROJECT TESTS
    // ------------------------------------------------------------------------

    @Test
    void createProject_AdminCanCreateForAnyUser() {
        // Arrange
        UUID targetUserId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        User targetUser = mockUser(targetUserId, Roles.USER);

        ExternalProject project = new ExternalProject();
        project.setName("Project A");

        when(userService.getUserById(targetUserId)).thenReturn(targetUser);
        when(externalProjectRepository.save(project))
                .thenAnswer(inv -> {
                    project.setId(projectId);
                    return project;
                });

        securityUtilsMock.when(com.example.challenge.utils.SecurityUtils::isAdmin)
                .thenReturn(true);

        // Act
        ProjectDTO dto = externalProjectService.createProject(
                targetUserId,
                project,
                "any-user"
        );

        // Assert
        assertEquals(projectId, dto.getId());
        assertEquals("Project A", dto.getName());
        verify(externalProjectRepository).save(project);
    }

    @Test
    void createProject_NormalUserCanCreateOnlyForHimself() {
        UUID userId = UUID.randomUUID();
        User user = mockUser(userId, Roles.USER);

        ExternalProject project = new ExternalProject();
        project.setName("My Own Project");

        when(userService.getUserById(userId)).thenReturn(user);

        securityUtilsMock.when(com.example.challenge.utils.SecurityUtils::isAdmin)
                .thenReturn(false);

        when(externalProjectRepository.save(project))
                .thenReturn(project);

        // Act
        ProjectDTO dto = externalProjectService.createProject(
                userId,
                project,
                userId.toString()
        );

        assertEquals("My Own Project", dto.getName());
        verify(externalProjectRepository).save(project);
    }

    @Test
    void createProject_NormalUserCreatingForAnotherUser_ShouldFail() {
        UUID targetUserId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        when(userService.getUserById(targetUserId))
                .thenReturn(mockUser(targetUserId, Roles.USER));

        securityUtilsMock.when(com.example.challenge.utils.SecurityUtils::isAdmin)
                .thenReturn(false);

        ExternalProject project = new ExternalProject();
        project.setName("Invalid");

        // Act & Assert
        assertThrows(SecurityException.class, () ->
                externalProjectService.createProject(
                        targetUserId,
                        project,
                        currentUserId.toString()
                )
        );
    }

    // ------------------------------------------------------------------------
    // GET PROJECTS BY USER TESTS
    // ------------------------------------------------------------------------

    @Test
    void getProjectsByUser_ShouldReturnProjects() {
        UUID userId = UUID.randomUUID();
        User user = mockUser(userId, Roles.USER);

        ExternalProject p1 = mockProject(UUID.randomUUID(), user, "A");
        ExternalProject p2 = mockProject(UUID.randomUUID(), user, "B");

        when(userService.getUserById(userId)).thenReturn(user);
        when(externalProjectRepository.findByUser(user))
                .thenReturn(List.of(p1, p2));

        List<ProjectDTO> result = externalProjectService.getProjectsByUser(userId);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
    }

    // ------------------------------------------------------------------------
    // DELETE PROJECT TESTS
    // ------------------------------------------------------------------------

    @Test
    void deleteProject_SuccessWhenUserOwnsProject() {
        UUID userId = UUID.randomUUID();
        User user = mockUser(userId, Roles.USER);

        ExternalProject project = mockProject(UUID.randomUUID(), user, "Test");

        when(externalProjectRepository.findById(project.getId()))
                .thenReturn(Optional.of(project));

        // Act
        externalProjectService.deleteProject(userId, project.getId());

        verify(externalProjectRepository).delete(project);
    }

    @Test
    void deleteProject_ShouldFail_WhenProjectBelongsToAnotherUser() {
        UUID userId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        User owner = mockUser(ownerId, Roles.USER);
        ExternalProject project = mockProject(UUID.randomUUID(), owner, "X");

        when(externalProjectRepository.findById(project.getId()))
                .thenReturn(Optional.of(project));

        assertThrows(IllegalStateException.class, () ->
                externalProjectService.deleteProject(userId, project.getId())
        );
    }

    @Test
    void deleteProject_ShouldFail_WhenProjectNotFound() {
        UUID projectId = UUID.randomUUID();

        when(externalProjectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                externalProjectService.deleteProject(UUID.randomUUID(), projectId)
        );
    }
}
