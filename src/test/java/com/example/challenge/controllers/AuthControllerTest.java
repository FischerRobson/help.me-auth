package com.example.challenge.controllers;

import com.example.challenge.model.Login;
import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.services.AuthService;
import com.example.challenge.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    // -----------------------------
    // LOGIN TEST
    // -----------------------------
    @Test
    void login_ShouldReturnJwtAndCookie() throws Exception {
        String jwt = "fake.jwt.token";
        when(authService.authenticate(any(Login.class))).thenReturn(jwt);

        Login login = new Login("test@mail.com", "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", jwt))
                .andExpect(header().exists("Set-Cookie"));
    }

    @Test
    void login_ShouldReturn401_WhenAuthFails() throws Exception {
        when(authService.authenticate(any(Login.class)))
                .thenThrow(new RuntimeException("Auth failed"));

        Login login = new Login("wrong@mail.com", "wrong");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().is4xxClientError());
    }

    // -----------------------------
    // CREATE USER
    // -----------------------------
    @Test
    void createUser_ShouldReturn201() throws Exception {
        User input = new User();
        input.setEmail("new@mail.com");
        input.setPassword("123456");
        input.setName("Robson");

        User saved = new User();
        saved.setId(UUID.randomUUID());
        saved.setEmail("new@mail.com");
        saved.setName("Robson");
        saved.setRole(Roles.USER);

        when(userService.createUser(any(User.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/auth/new-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.name").value("Robson"));
    }

    @Test
    void createUser_ShouldFail_OnInvalidEmail() throws Exception {
        User input = new User();
        input.setEmail("not-email");
        input.setPassword("123456");
        input.setName("Robson");

        mockMvc.perform(post("/auth/new-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    // -----------------------------
    // /me ENDPOINT
    // -----------------------------
    @Test
    void getCurrentUser_ShouldReturnCorrectData() throws Exception {
        mockMvc.perform(get("/auth/me")
                        .requestAttr("userEmail", "test@mail.com")
                        .requestAttr("userRole", "ADMIN")
                        .requestAttr("userId", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("User: test@mail.com (role: ADMIN, id: 123)"));
    }

    // -----------------------------
    // LOGOUT
    // -----------------------------
    @Test
    void logout_ShouldClearCookie() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Set-Cookie",
                        org.hamcrest.Matchers.containsString("Max-Age=0")));
    }

}
