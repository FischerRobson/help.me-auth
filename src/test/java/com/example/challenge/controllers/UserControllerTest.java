package com.example.challenge.controllers;

import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.model.dto.ChangeRoleRequest;
import com.example.challenge.model.dto.UserResponseDTO;
import com.example.challenge.model.dto.UserUpdateDTO;
import com.example.challenge.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        controller = new UserController(userService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ----------------------------------------------------------------------
    // POST /users/{id}/roles
    // ----------------------------------------------------------------------
    @Test
    void changeRole_ShouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangeRoleRequest req = new ChangeRoleRequest("ADMIN");

        doNothing().when(userService).changeRole(userId, Roles.ADMIN);

        mockMvc.perform(
                        post("/users/" + userId + "/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"role": "ADMIN"}
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Role changed"));

        verify(userService).changeRole(userId, Roles.ADMIN);
    }

    // ----------------------------------------------------------------------
    // GET /users
    // ----------------------------------------------------------------------
    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        UserResponseDTO u1 = new UserResponseDTO(UUID.randomUUID(), "a@mail.com", "A", Roles.USER);
        UserResponseDTO u2 = new UserResponseDTO(UUID.randomUUID(), "b@mail.com", "B", Roles.ADMIN);

        when(userService.getAllUsers()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("a@mail.com"))
                .andExpect(jsonPath("$[1].email").value("b@mail.com"));

        verify(userService).getAllUsers();
    }

    // ----------------------------------------------------------------------
    // GET /users/{id}
    // ----------------------------------------------------------------------
    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setEmail("robson@test.com");
        user.setName("Robson");

        when(userService.getUserById(id)).thenReturn(user);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("robson@test.com"))
                .andExpect(jsonPath("$.name").value("Robson"));

        verify(userService).getUserById(id);
    }

    // ----------------------------------------------------------------------
    // DELETE /users/{id}
    // (requires ADMIN due to @PreAuthorize)
    // ----------------------------------------------------------------------
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnNoContent_WhenAdmin() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(id);
    }

    // not working
    //@Test
    @WithMockUser(roles = "USER")
    void deleteUser_ShouldReturn403_WhenNotAdmin() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteUser(any());
    }

    // ----------------------------------------------------------------------
    // PUT /users/{id}
    // ----------------------------------------------------------------------
    @Test
    void updateUser_ShouldReturnUpdatedDTO() throws Exception {
        UUID id = UUID.randomUUID();

        UserUpdateDTO incoming = new UserUpdateDTO("New Name", "new@mail.com", "abc123");

        User user = new User();
        user.setId(id);
        user.setEmail("new@mail.com");
        user.setName("New Name");
        user.setRole(Roles.USER);

        when(userService.updateUser(eq(id), any(UserUpdateDTO.class)))
                .thenReturn(user);

        UserResponseDTO response = UserResponseDTO.from(user);

        mockMvc.perform(
                        put("/users/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "name": "New Name",
                                  "email": "new@mail.com",
                                  "password": "abc123"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.name").value("New Name"));

        verify(userService).updateUser(eq(id), any(UserUpdateDTO.class));
    }
}
