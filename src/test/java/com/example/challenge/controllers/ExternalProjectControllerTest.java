package com.example.challenge.controllers;

import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.dto.ProjectDTO;
import com.example.challenge.services.ExternalProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExternalProjectControllerTest {

    @Mock
    private ExternalProjectService externalProjectService;

    @InjectMocks
    private ExternalProjectController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        externalProjectService = mock(ExternalProjectService.class);

        controller = new ExternalProjectController(externalProjectService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Helper to inject request attribute "userId" as your controller expects.
     */
    private RequestPostProcessor withUserId(String userId) {
        return request -> {
            request.setAttribute("userId", userId);
            return request;
        };
    }

    // ----------------------------------------------------------------------
    // POST /external-projects/{userId}
    // ----------------------------------------------------------------------
    @Test
    void createProject_ShouldReturnProjectDTO() throws Exception {
        UUID targetUserId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        String currentUserId = targetUserId.toString(); // allowed

        ExternalProject project = new ExternalProject();
        project.setName("My Project");

        ProjectDTO dto = new ProjectDTO(projectId, "My Project");

        when(externalProjectService.createProject(eq(targetUserId), any(ExternalProject.class), eq(currentUserId)))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/external-projects/" + targetUserId)
                                .with(withUserId(currentUserId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "name": "My Project"
                        }
                        """)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()))
                .andExpect(jsonPath("$.name").value("My Project"));

        verify(externalProjectService).createProject(eq(targetUserId), any(), eq(currentUserId));
    }

    // ----------------------------------------------------------------------
    // GET /external-projects/{userId}
    // ----------------------------------------------------------------------
    @Test
    void listUserProjects_ShouldReturnList() throws Exception {
        UUID userId = UUID.randomUUID();

        ProjectDTO p1 = new ProjectDTO(UUID.randomUUID(), "Proj A");
        ProjectDTO p2 = new ProjectDTO(UUID.randomUUID(), "Proj B");

        when(externalProjectService.getProjectsByUser(userId))
                .thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/external-projects/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Proj A"))
                .andExpect(jsonPath("$[1].name").value("Proj B"));

        verify(externalProjectService).getProjectsByUser(userId);
    }

    // ----------------------------------------------------------------------
    // DELETE /external-projects/{userId}/{projectId}
    // ----------------------------------------------------------------------
    @Test
    void deleteProject_ShouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        doNothing().when(externalProjectService).deleteProject(userId, projectId);

        mockMvc.perform(delete("/external-projects/" + userId + "/" + projectId))
                .andExpect(status().isNoContent());

        verify(externalProjectService).deleteProject(userId, projectId);
    }
}
